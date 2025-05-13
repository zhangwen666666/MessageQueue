package com.zw.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(topic = "bootTestTopic", consumerGroup = "boot-consumer-group")
public class SimpleMsgListener implements RocketMQListener<MessageExt> {

    /**
     * 这个方法就是消费者的方法
     *
     * @param message
     */
    @Override
    public void onMessage(MessageExt message) {
        System.out.println("消费者收到消息：" + new String(message.getBody()));
    }
}
