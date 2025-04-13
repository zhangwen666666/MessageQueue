package com.zw.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Orders implements Serializable {
    private static final long serialVersionUID = 1L;
    private String orderId;
    private String orderName;
    private Date orderTime;
    private BigDecimal orderPrice;
}
