package com.zw.service;

public interface GoodsService {
    // 秒杀
    void realSeckill(Integer goodsId, Integer userId);
}
