package com.zw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(){
        // 创建消息属性对象
        MessageProperties messageProperties = new MessageProperties();
        Map<String, Object> headersValue = new HashMap<>();
        // 发送到B队列
        headersValue.put("type", "s");
        headersValue.put("status", "0");
        messageProperties.setHeaders(headersValue); // 设置消息头

        // 消息
        Message message = MessageBuilder.withBody("头部交换机".getBytes()).
                andProperties(messageProperties).build();

        rabbitTemplate.convertAndSend("exchange.headers", "", message);
        log.info("发送消息完毕，发送时间：{}", new Date());
    }
}
