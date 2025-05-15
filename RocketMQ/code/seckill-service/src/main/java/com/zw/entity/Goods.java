package com.zw.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

/**
 * goods
 */
@Data
public class Goods implements Serializable {
    private Integer id;

    private String goodsName;

    private BigDecimal price;

    private Integer stocks;

    private Integer status;

    private String pic;

    private Date createTime;

    private Date updateTime;

    /**
     * 是否参与秒杀 1是0否
     */
    private Integer spike;

    private static final long serialVersionUID = 1L;
}