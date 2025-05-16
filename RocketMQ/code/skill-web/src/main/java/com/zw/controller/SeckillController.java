package com.zw.controller;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class SeckillController {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    AtomicInteger userIdAt = new AtomicInteger(0);

    /**
     * 秒杀
     *
     * @param goodsId 商品id
     * @param userId  用户id，真实项目用户需要登录，用户id不用传递，是从SpringSecurity中获取
     * @return
     */
    @GetMapping("seckill")
    public String doSeckill(Integer goodsId/*, Integer userId*/) {
        // 1. 用户去重 uk uniqueKey = goodsId + userId
        int userId = userIdAt.incrementAndGet(); // 模拟用户id
        String uk = goodsId + "-" + userId;
        Boolean flag = redisTemplate.opsForValue().setIfAbsent(uk, "");//setnx
        if (!flag) {
            return "重复秒杀, 参与失败O(n_n)O~";
        }

        // 2. 库存的预扣减 redis中存入 goodsId:stock
        // 先查询后修改是线程不安全的，所以直接使用redis的derc key命令
        // redisTemplate中该命令的api接口是decrement,返回值是减一之后的结果
        Long count = redisTemplate.opsForValue().decrement("goodsId:" + goodsId);
        if (count < 0) {
            return "库存不足，秒杀失败O(n_n)O~";
        }

        // 3. 消息放入mq 异步处理
        rocketMQTemplate.asyncSend("seckillTopic4", uk, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable e) {
                System.out.println("用户id：" + userId + ", 商品id：" + goodsId + "\t发送失败" + e.getMessage());
            }
        });
        return "正在拼命抢购中,请稍后到订单中心查看";
    }
}
