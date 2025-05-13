package com.zw.listener;

import com.alibaba.fastjson.JSON;
import com.zw.domain.Order;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
        topic = "bootOrderTopic",
        consumerGroup = "boot-orderly-consumer-group",
        consumeMode = ConsumeMode.ORDERLY, // 顺序消费模式
        maxReconsumeTimes = 3 // 最大重试次数
)
public class OrderlyMsgListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt messageExt) {
        Order order = JSON.parseObject(new String(messageExt.getBody()), Order.class);
        System.out.println("接收到消息：" + order);
    }
}
