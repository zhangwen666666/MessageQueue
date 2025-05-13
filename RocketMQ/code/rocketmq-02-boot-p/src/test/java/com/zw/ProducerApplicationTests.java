package com.zw;

import com.alibaba.fastjson.JSON;
import com.zw.domain.Order;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
class ProducerApplicationTests {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Test
    public void producerTest() {
        // 发送同步消息
        rocketMQTemplate.syncSend("bootTestTopic", "我是一个同步消息");

        // 发送异步消息
        rocketMQTemplate.asyncSend("bootTestTopic", "我是一个异步消息", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("接收异步消息成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("接收异步消息失败");
            }
        });

        // 发送单向消息
        rocketMQTemplate.sendOneWay("bootTestTopic", "我发送的是单向消息");

        // 发送延时消息
        Message<String> message = MessageBuilder.withPayload("我是一个延时消息").build();
        rocketMQTemplate.syncSend("bootTestTopic", message, 10000, 3);

        // 发送顺序消息
        // 创建订单信息
        List<Order> orders = Arrays.asList(
                new Order("111", 1, "下单"),
                new Order("111", 1, "物流"),
                new Order("111", 1, "签收"),
                new Order("222", 2, "下单"),
                new Order("222", 2, "物流"),
                new Order("222", 2, "拒收")
        );
        orders.forEach(order -> {
            // 一般我们发送对象的时候都是使用json
            rocketMQTemplate.syncSendOrderly("bootOrderTopic", JSON.toJSONString(order), order.getOrderSn());
        });

        // 发送一个带tag的消息
        rocketMQTemplate.syncSend("bootTagTopic:tagA", "我是一个带tag的消息");

        MessageBuilder.withPayload("我是一个带key的消息").
    }
}
