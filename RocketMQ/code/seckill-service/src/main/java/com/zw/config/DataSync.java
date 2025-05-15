package com.zw.config;

import com.zw.entity.Goods;
import com.zw.mapper.GoodsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DataSync {
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /*@Scheduled(cron = "")
    public void sync() {
        System.out.println("数据同步");
    }*/


    /**
     * 希望这个方法再项目启动以后
     * 并且这个类的属性注入完毕之后执行
     */
    @PostConstruct
    public void initData(){
        // 查询所有参与秒杀的商品
        List<Goods> goodsList = goodsMapper.selectSeckillGoods();
        if (CollectionUtils.isEmpty(goodsList)){
            return;
        }
        // 写入redis
        goodsList.forEach(goods -> {
            redisTemplate.opsForValue().set("goodsId:" + goods.getId(), goods.getStocks().toString());
        });
    }
}
