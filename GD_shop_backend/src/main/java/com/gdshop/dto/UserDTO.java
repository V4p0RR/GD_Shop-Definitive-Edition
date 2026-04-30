package com.gdshop.dto;

import lombok.Data;

@Data
public class UserDTO {
    private Long id;
    private String nickName;
    private String icon;
    private Integer role;
    private String defaultAddress;
    private String defaultReceiver;
    private String defaultPhone;
}
