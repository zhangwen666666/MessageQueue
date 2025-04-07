package com.zw.message;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class MessageReceive {
    /**
     * 接收消息
     * @param message 消息
     * @param channel 信道 所有的消息接收和发送都通过信道
     */
    @RabbitListener(queues = {"queue.normal.5"})
    public void receive(Message message, Channel channel) {
        // 获取消息属性
        MessageProperties messageProperties = message.getMessageProperties();
        // 获取消息的唯一标识
        long deliveryTag = messageProperties.getDeliveryTag();
        try{
            // 接收消息并做后序处理，例如更新数据库
            String msg = new String(message.getBody());
            log.info("接收到消息：{}, 接收时间为{}", msg, new Date());
            int i = 1/0;
            // 处理成功，确认消息
            // void basicAck(long deliveryTag, boolean multiple)
            // deliveryTag：消息的一个数字标签，唯一标识
            // multiple如果是true表示对小于deliveryTag标签下的消息都进行确认，false表示只确认当前的一条消息
            channel.basicAck(deliveryTag, false);
        } catch (Exception e){
            log.error("接收者出现问题，原因是{}", e.getMessage());
            try {
                // 拒绝消息并重新入队
                //channel.basicNack(deliveryTag, false, true);
                channel.basicReject(deliveryTag, false);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
