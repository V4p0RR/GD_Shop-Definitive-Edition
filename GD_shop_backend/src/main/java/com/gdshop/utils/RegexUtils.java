package com.gdshop.utils;

import cn.hutool.core.util.StrUtil;

public class RegexUtils {
    public static boolean isPhoneInvalid(String phone) {
        return StrUtil.isBlank(phone) || !phone.matches("^1[3-9]\\d{9}$");
    }
}
