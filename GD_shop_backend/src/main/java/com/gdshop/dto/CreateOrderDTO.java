package com.gdshop.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderDTO {
    private String address;
    private String phone;
    private String receiver;
    private String remark;
    private List<Long> skuIds;
    private List<Integer> quantities;
}
