package com.zw.listener;

import com.zw.service.GoodsService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "seckillTopic",
        consumerGroup = "seckill-consumer-group",
        consumeMode = ConsumeMode.CONCURRENTLY,
        consumeThreadNumber = 40
)
public class SeckillListener implements RocketMQListener<MessageExt> {
    @Autowired
    private GoodsService goodsService;

    /**
     * 1. 扣减库存
     * 2. 写订单表
     * @param messageExt
     */
    @Override
    public void onMessage(MessageExt messageExt) {
        String msg = new String(messageExt.getBody());
        String[] split = msg.split("-");
        Integer goodsId = Integer.parseInt(split[0]);
        Integer userId = Integer.parseInt(split[1]);
        goodsService.realSeckill(goodsId, userId); // 真正处理秒杀业务
    }
}
