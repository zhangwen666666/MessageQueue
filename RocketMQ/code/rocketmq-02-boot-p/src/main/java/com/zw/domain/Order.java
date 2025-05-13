package com.zw.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    private String orderSn; //订单编号
    private Integer userId; // 用户id
    private String desc; // 描述信息
}
