package com.gdshop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gdshop.dto.LoginFormDTO;
import com.gdshop.dto.Result;
import com.gdshop.entity.User;

import javax.servlet.http.HttpServletRequest;

public interface IUserService extends IService<User> {
    Result sendCode(String phone);
    Result login(LoginFormDTO loginForm);
    Result logout(HttpServletRequest request);
    Result sign();
    Result signCount();
    Result adminUserList(Integer current, String keyword);
    Result updateUserStatus(Long id, Integer enabled);
    Result updateUserRole(Long id, Integer role);
    Result deleteUser(Long id);
    Result changePassword(String oldPassword, String newPassword);
    Result updateAddress(String defaultAddress, String defaultReceiver, String defaultPhone);
}
