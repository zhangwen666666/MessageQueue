package com.zw.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * order_records
 */
@Data
public class OrderRecords implements Serializable {
    private Integer id;

    private Integer userId;

    private String orderSn;

    private Integer goodsId;

    private Date createTime;

    private static final long serialVersionUID = 1L;
}