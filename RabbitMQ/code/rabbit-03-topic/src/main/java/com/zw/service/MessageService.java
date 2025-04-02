package com.zw.service;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private AmqpTemplate amqpTemplate; // amqp是协议，RabbitTemplate是amqp的实现

    public void sendMessage(){
        Message message = MessageBuilder.withBody("法外狂徒张三".getBytes()).build();
        amqpTemplate.convertAndSend("exchange.topic", "hello.world", message);
    }
}
