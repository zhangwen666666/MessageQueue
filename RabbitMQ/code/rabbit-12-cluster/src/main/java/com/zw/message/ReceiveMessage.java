package com.zw.message;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ReceiveMessage {
     @RabbitListener(queues = "${my.queueName}")
    public void receiveMsg(Message message, Channel channel){
        // 获取消息的唯一标识
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            log.info("接收到消息：{}", new String(message.getBody()));
            channel.basicAck(deliveryTag, false);
        } catch (Exception e){
            log.error("消息处理出现问题");
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            throw new RuntimeException(e);
        }
    }
}
