package com.zw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class MessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendMessage(){
        // 定义要发送的消息
        String msg = "helloworld";
        Message message = new Message(msg.getBytes());
        // 发送消息
        // 参数1：交换机名称，
        // 参数2：路由key， (扇形交换机不需要路由key，它将消息广播给与它相连的所有消息队列)
        // 参数3：消息
        rabbitTemplate.convertAndSend("exchange.fanout", "", message);
        log.info("发送消息完毕，发送时间：{}", new Date());
    }
}
