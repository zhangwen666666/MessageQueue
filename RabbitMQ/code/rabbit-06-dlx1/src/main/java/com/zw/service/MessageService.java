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

    public void sendMsg(){
        Message message = MessageBuilder.withBody("订单1".getBytes()).build();
        // 发送消息
        rabbitTemplate.convertAndSend("exchange.normal.a", "order", message);
        log.info("发送消息完毕，发送时间：{}", new Date());
    }
}
