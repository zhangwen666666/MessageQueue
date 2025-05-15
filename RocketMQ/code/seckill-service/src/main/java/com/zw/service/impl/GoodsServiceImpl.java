package com.zw.service.impl;

import com.zw.entity.Goods;
import com.zw.entity.OrderRecords;
import com.zw.mapper.GoodsMapper;
import com.zw.mapper.OrderRecordsMapper;
import com.zw.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private OrderRecordsMapper orderRecordsMapper;

    @Override
    // 保证 扣减库存和写订单表的原子性
    @Transactional(rollbackFor = Exception.class)
    public void realSeckill(Integer goodsId, Integer userId) {
        // 扣减库存
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        int finalStock = goods.getStocks() - 1;
        if (finalStock < 0) {
            throw new RuntimeException("商品" + goodsId + "库存不足, 用户id为：" + userId);
        }
        goods.setStocks(finalStock);
        goods.setUpdateTime(new Date());
        int i = goodsMapper.updateByPrimaryKeySelective(goods);
        if (i > 0){
            // 写订单表
            OrderRecords orderRecords = new OrderRecords();
            orderRecords.setUserId(userId);
            orderRecords.setGoodsId(goodsId);
            orderRecords.setCreateTime(new Date());
            orderRecordsMapper.insert(orderRecords);
        }
    }
}
