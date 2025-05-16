package com.zw.listener;

import com.zw.service.GoodsService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "seckillTopic4",
        consumerGroup = "seckill-consumer-group4",
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 40
)
public class SeckillListener implements RocketMQListener<MessageExt> {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final int SPIN_TIME = 20000; // 自旋总时间

    /**
     * 1. 扣减库存
     * 2. 写订单表
     * @param messageExt
     */
/*

    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        String[] split = msg.split("-");
        Integer goodsId = Integer.parseInt(split[0]);
        Integer userId = Integer.parseInt(split[1]);
        // 方案一 ：在事务外面加锁可以实现安全并发
        // 只能部署在同一个机器中，不能部署在集群上
        // 因为不同机器的jvm不同，锁是不共享的
        synchronized (this) {
            goodsService.realSeckill(goodsId, userId); // 真正处理秒杀业务
        }
    }
*/

    // 方案二：使用分布式锁 mysql(行锁) redis
    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        String[] split = msg.split("-");
        Integer goodsId = Integer.parseInt(split[0]);
        Integer userId = Integer.parseInt(split[1]);
        int currentThreadTime = 0;
        // 循环拿锁, 也可以写成死循环
        while (currentThreadTime < SPIN_TIME){
            Boolean flag = redisTemplate.opsForValue().setIfAbsent("lock:" + goodsId, "");
            if (flag) {
                try {
                    goodsService.realSeckill(goodsId, userId); // 真正处理秒杀业务
                    return; // 处理完业务退出循环
                } finally {
                    // 不管是否成功执行，都要释放锁
                    redisTemplate.delete("lock:" + goodsId);
                }
            } else {
                // 没有拿到锁，先睡眠200ms，再执行
                currentThreadTime += 200;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
