package com.zw.manage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.zw.vo.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;

@Component
@Slf4j
public class ReceiveMessage {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @RabbitListener(queues = "${my.queueName}")
    public void receiveMessage(Message message, Channel channel) throws JsonProcessingException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag(); // 获取消息的唯一标识
        String strOrder = new String(message.getBody());
        Orders order = objectMapper.readValue(strOrder, Orders.class); // 将消息转换为对象
        try{
            boolean flag = redisTemplate.opsForValue().setIfAbsent("idempotent:" + order.getOrderId(), String.valueOf(order.getOrderId()));
            if (flag) { //key不存在返回true
                //相当于是第一次消费该消息
                //TODO 处理业务
                log.info("接收到消息：{}", strOrder);
                log.info("接收到消息时间：{}", new Date());
            }
            // 手动确认
            channel.basicAck(deliveryTag, false);
        } catch (Exception e){
            log.error("消息处理出现问题");
            try {
                channel.basicNack(deliveryTag, false, true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
