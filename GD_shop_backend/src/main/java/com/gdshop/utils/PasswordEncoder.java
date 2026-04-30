package com.gdshop.utils;

import cn.hutool.core.util.StrUtil;

public class PasswordEncoder {
    public static String encode(String rawPassword) {
        return cn.hutool.crypto.digest.DigestUtil.md5Hex(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        if (StrUtil.isBlank(encodedPassword)) {
            return false;
        }
        return encodedPassword.equals(encode(rawPassword));
    }
}
