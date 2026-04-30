package com.gdshop.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gdshop.dto.LoginFormDTO;
import com.gdshop.dto.Result;
import com.gdshop.dto.UserDTO;
import com.gdshop.entity.User;
import com.gdshop.mapper.UserMapper;
import com.gdshop.service.IUserService;
import com.gdshop.utils.RedisConstants;
import com.gdshop.utils.RegexUtils;
import com.gdshop.utils.SystemConstants;
import com.gdshop.utils.UserHolder;
import com.gdshop.utils.PasswordEncoder;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result sendCode(String phone) {
        if (RegexUtils.isPhoneInvalid(phone)) {
            return Result.fail("手机号格式错误");
        }
        String code = RandomUtil.randomNumbers(6);
        stringRedisTemplate.opsForValue().set(RedisConstants.LOGIN_CODE_KEY + phone, code,
                RedisConstants.LOGIN_CODE_TTL, TimeUnit.MINUTES);
        log.debug("验证码发送成功: {}", code);
        return Result.ok(code);
    }

    @Override
    public Result login(LoginFormDTO loginForm) {
        if (RegexUtils.isPhoneInvalid(loginForm.getPhone())) {
            return Result.fail("手机号格式错误");
        }

        User user;
        // 验证码登录
        if (StrUtil.isNotBlank(loginForm.getCode())) {
            String cacheCode = stringRedisTemplate.opsForValue().get(RedisConstants.LOGIN_CODE_KEY + loginForm.getPhone());
            if (cacheCode == null || !cacheCode.equals(loginForm.getCode())) {
                return Result.fail("验证码错误");
            }
            user = query().eq("phone", loginForm.getPhone()).one();
            if (user == null) {
                user = new User();
                user.setPhone(loginForm.getPhone());
                user.setNickName(SystemConstants.USER_NICK_NAME_PREFIX + RandomUtil.randomString(10));
                user.setRole(0);
                user.setCreateTime(LocalDateTime.now());
                user.setUpdateTime(LocalDateTime.now());
                save(user);
            }
        } else {
            // 密码登录
            if (StrUtil.isBlank(loginForm.getPassword())) {
                return Result.fail("请输入密码");
            }
            user = query().eq("phone", loginForm.getPhone()).one();
            if (user == null) {
                return Result.fail("用户不存在");
            }
            if (!PasswordEncoder.matches(loginForm.getPassword(), user.getPassword())) {
                return Result.fail("密码错误");
            }
        }

        String token = UUID.randomUUID().toString(true);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue != null ? fieldValue.toString() : null));
        stringRedisTemplate.opsForHash().putAll(RedisConstants.LOGIN_USER_KEY + token, userMap);
        stringRedisTemplate.expire(RedisConstants.LOGIN_USER_KEY + token, RedisConstants.LOGIN_USER_TTL, TimeUnit.SECONDS);
        log.info("登录成功, token:{}", token);
        return Result.ok(token);
    }

    @Override
    public Result logout(HttpServletRequest request) {
        String token = request.getHeader("authorization");
        if (token != null) {
            stringRedisTemplate.delete(RedisConstants.LOGIN_USER_KEY + token);
        }
        UserHolder.removeUser();
        return Result.ok();
    }

    @Override
    public Result sign() {
        Long userId = UserHolder.getUser().getId();
        if (userId == null || userId == 0) {
            return Result.fail("用户未登录");
        }
        LocalDateTime now = LocalDateTime.now();
        String keySuffix = now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        String key = "sign:" + userId + keySuffix;
        int dayOfMonth = now.getDayOfMonth();
        stringRedisTemplate.opsForValue().setBit(key, dayOfMonth - 1, true);
        return Result.ok();
    }

    @Override
    public Result signCount() {
        Long userId = UserHolder.getUser().getId();
        if (userId == null || userId == 0) {
            return Result.fail("用户未登录");
        }
        LocalDateTime now = LocalDateTime.now();
        String key = "sign:" + userId + now.format(DateTimeFormatter.ofPattern(":yyyyMM"));
        int dayOfMonth = now.getDayOfMonth();
        List<Long> list = stringRedisTemplate.opsForValue().bitField(
                key, BitFieldSubCommands.create()
                        .get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth))
                        .valueAt(0));
        if (list == null || list.isEmpty()) {
            return Result.ok(0);
        }
        Long sign = list.get(0);
        if (sign == null || sign == 0) {
            return Result.ok(0);
        }
        int count = 0;
        for (int i = 0; i < dayOfMonth; i++) {
            if ((sign & 1) == 0) break;
            count++;
            sign >>>= 1;
        }
        return Result.ok(count);
    }

    @Override
    public Result adminUserList(Integer current, String keyword) {
        com.baomidou.mybatisplus.extension.plugins.pagination.Page<User> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(current, 10);
        com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User> wrapper =
                new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<>();
        if (StrUtil.isNotBlank(keyword)) {
            wrapper.like(User::getNickName, keyword).or().like(User::getPhone, keyword);
        }
        wrapper.orderByDesc(User::getCreateTime);
        page(page, wrapper);
        List<UserDTO> list = page.getRecords().stream()
                .map(u -> BeanUtil.copyProperties(u, UserDTO.class))
                .collect(java.util.stream.Collectors.toList());
        return Result.ok(list, page.getTotal());
    }

    @Override
    public Result updateUserStatus(Long id, Integer enabled) {
        User user = getById(id);
        if (user == null) return Result.fail("用户不存在");
        user.setRole(enabled != null && enabled == 1 ? 1 : 0);
        updateById(user);
        return Result.ok();
    }

    @Override
    public Result updateUserRole(Long id, Integer role) {
        User user = getById(id);
        if (user == null) return Result.fail("用户不存在");
        user.setRole(role != null ? role : 0);
        updateById(user);
        return Result.ok();
    }

    @Override
    public Result deleteUser(Long id) {
        UserDTO caller = UserHolder.getUser();
        if (caller == null || caller.getRole() == null || caller.getRole() != 1) {
            return Result.fail("无管理员权限");
        }
        User user = getById(id);
        if (user == null) return Result.fail("用户不存在");
        if (user.getRole() != null && user.getRole() == 1) {
            return Result.fail("不能删除管理员账号");
        }
        removeById(id);
        return Result.ok();
    }

    @Override
    public Result changePassword(String oldPassword, String newPassword) {
        UserDTO currentUser = UserHolder.getUser();
        if (currentUser == null) return Result.fail("未登录");
        User user = getById(currentUser.getId());
        if (user == null) return Result.fail("用户不存在");
        if (!PasswordEncoder.matches(oldPassword, user.getPassword())) {
            return Result.fail("旧密码错误");
        }
        user.setPassword(PasswordEncoder.encode(newPassword));
        updateById(user);
        return Result.ok();
    }

    @Override
    public Result updateAddress(String defaultAddress, String defaultReceiver, String defaultPhone) {
        UserDTO currentUser = UserHolder.getUser();
        if (currentUser == null) return Result.fail("未登录");
        User user = getById(currentUser.getId());
        if (user == null) return Result.fail("用户不存在");
        user.setDefaultAddress(defaultAddress);
        user.setDefaultReceiver(defaultReceiver);
        user.setDefaultPhone(defaultPhone);
        updateById(user);
        return Result.ok();
    }
}
