package com.zw.message;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class ReceiveMessage {
    @RabbitListener(queues={"queue.delay.4"})
    public void receiveMessage(Message message){
        String body = new String(message.getBody());
        log.info("接收到消息：{}, 时间为{}", body, new Date());
    }
}
