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
/*
    @Override
    // 保证 扣减库存和写订单表的原子性
    @Transactional(rollbackFor = Exception.class)
    public void realSeckill(Integer goodsId, Integer userId) {
        // 扣减库存
        // 这里的锁仍然是线程不安全的
        // 因为事务是以AOP的方式添加的，是在方法执行前开启事务，方法执行结束后提交事务
        // AB两个线程可能同事开启事务，读到相同的数据，导致线程不安全，（不可重复读隔离级别）
        // 因此锁要加在调用该方法的地方才安全
        // synchronized (this) {
            Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
            int finalStock = goods.getStocks() - 1;
            if (finalStock < 0) {
                throw new RuntimeException("商品" + goodsId + "库存不足, 用户id为：" + userId);
            }
            goods.setStocks(finalStock);
            goods.setUpdateTime(new Date());
            int i = goodsMapper.updateByPrimaryKeySelective(goods);
            if (i > 0) {
                // 写订单表
                OrderRecords orderRecords = new OrderRecords();
                orderRecords.setUserId(userId);
                orderRecords.setGoodsId(goodsId);
                orderRecords.setCreateTime(new Date());
                orderRecordsMapper.insert(orderRecords);
            }
        // }
    }
*/

/*
    @Override
    // 方法二：使用mysql行锁解决
    @Transactional(rollbackFor = Exception.class)
    public void realSeckill(Integer goodsId, Integer userId) {
        // update goods set stocks = stocks - 1, update_time = now() where id = goodsId
        int i = goodsMapper.updateStock(goodsId);
        if (i > 0) {
            // 写订单表
            OrderRecords orderRecords = new OrderRecords();
            orderRecords.setUserId(userId);
            orderRecords.setGoodsId(goodsId);
            orderRecords.setCreateTime(new Date());
            orderRecordsMapper.insert(orderRecords);
        }
    }
*/


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void realSeckill(Integer goodsId, Integer userId) {
        Goods goods = goodsMapper.selectByPrimaryKey(goodsId);
        int finalStock = goods.getStocks() - 1;
        if (finalStock < 0) {
            throw new RuntimeException("商品" + goodsId + "库存不足, 用户id为：" + userId);
        }
        goods.setStocks(finalStock);
        goods.setUpdateTime(new Date());
        int i = goodsMapper.updateByPrimaryKeySelective(goods);
        if (i > 0) {
            // 写订单表
            OrderRecords orderRecords = new OrderRecords();
            orderRecords.setUserId(userId);
            orderRecords.setGoodsId(goodsId);
            orderRecords.setCreateTime(new Date());
            orderRecordsMapper.insert(orderRecords);
        }
    }
}
