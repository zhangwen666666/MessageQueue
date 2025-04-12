package com.zw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
@Slf4j
public class MessageService{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnsCallback((returnedMessage) -> log.error("消息没有正确路由到消息队列,原因为{}",returnedMessage.getReplyText()));
    }

    public void sendMessage() {

        // 创建消息
        Message message = MessageBuilder.withBody("消息".getBytes()).build();
        // 发送消息
        rabbitTemplate.convertAndSend("exchange.return.b", "error", message);
        log.info("发送消息完毕，发送时间：{}", new Date());
    }

//    @Override
//    public void returnedMessage(ReturnedMessage returnedMessage) {
//        log.error("消息没有正确路由到消息队列,原因为{}",returnedMessage.getReplyText());
//    }
}
