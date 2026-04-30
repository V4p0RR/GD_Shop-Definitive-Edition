package com.gdshop.controller;

import com.gdshop.dto.LoginFormDTO;
import com.gdshop.dto.Result;
import com.gdshop.dto.UserDTO;
import com.gdshop.entity.User;
import com.gdshop.service.IUserService;
import com.gdshop.utils.UserHolder;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @PostMapping("code")
    public Result sendCode(@RequestParam("phone") String phone) {
        return userService.sendCode(phone);
    }

    @PostMapping("/login")
    public Result login(@RequestBody LoginFormDTO loginForm) {
        return userService.login(loginForm);
    }

    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        return userService.logout(request);
    }

    @GetMapping("/me")
    public Result me() {
        UserDTO current = UserHolder.getUser();
        if (current == null) return Result.fail("未登录");
        User user = userService.getById(current.getId());
        if (user == null) return Result.fail("用户不存在");
        return Result.ok(BeanUtil.copyProperties(user, UserDTO.class));
    }

    @GetMapping("/{id}")
    public Result queryUserById(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) {
            return Result.ok();
        }
        return Result.ok(BeanUtil.copyProperties(user, UserDTO.class));
    }

    @PostMapping("/sign")
    public Result sign() {
        return userService.sign();
    }

    @GetMapping("/sign/count")
    public Result signCount() {
        return userService.signCount();
    }

    @GetMapping("/admin/list")
    public Result adminUserList(@RequestParam(defaultValue = "1") Integer current,
                                @RequestParam(required = false) String keyword) {
        return userService.adminUserList(current, keyword);
    }

    @PutMapping("/{id}/status")
    public Result updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return userService.updateUserStatus(id, body.get("enabled"));
    }

    @PutMapping("/{id}/role")
    public Result updateUserRole(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        return userService.updateUserRole(id, body.get("role"));
    }

    @DeleteMapping("/{id}")
    public Result deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }

    @PutMapping("/password")
    public Result changePassword(@RequestBody Map<String, String> body) {
        return userService.changePassword(body.get("oldPassword"), body.get("newPassword"));
    }

    @PutMapping("/address")
    public Result updateAddress(@RequestBody Map<String, String> body) {
        return userService.updateAddress(
                body.get("defaultAddress"),
                body.get("defaultReceiver"),
                body.get("defaultPhone"));
    }
}
