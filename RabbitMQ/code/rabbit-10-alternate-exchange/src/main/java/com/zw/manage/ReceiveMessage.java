package com.zw.manage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ReceiveMessage {
    @RabbitListener(queues = "queue.alternate.a")
    public void receiveMessage(Message message) {
        log.info("接收到消息：{}", new String(message.getBody()));
        log.info("接收到消息时间：{}", new Date());
    }
}
