package com.zw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    public void sendMsg(){
//        // 消息属性
//        MessageProperties messageProperties = new MessageProperties();
//        messageProperties.setExpiration("30000"); // 设置过期时间，单位为毫秒
//        // 创建消息
//        Message message = MessageBuilder.withBody("订单5".getBytes()).andProperties(messageProperties).build();
//        // 发送消息
//        rabbitTemplate.convertAndSend("exchange.normal.2", "order", message);
//        log.info("发送消息完毕，发送时间：{}", new Date());
//    }


    public void sendMsg(){
        String str = "订单1";
        for (int i = 0; i < 1; i++) {
            // 创建消息
            Message message = MessageBuilder.withBody((str + i).getBytes()).build();
            // 发送消息
            rabbitTemplate.convertAndSend("exchange.normal.5", "order", message);
            log.info("发送消息完毕，发送时间：{}", new Date());
        }
    }
}
