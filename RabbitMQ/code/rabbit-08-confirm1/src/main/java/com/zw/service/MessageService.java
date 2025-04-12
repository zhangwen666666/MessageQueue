package com.zw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.connection.CorrelationData;
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

    @PostConstruct // 构造方法执行之后执行，相当于初始化作用
    public void init(){
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            String id = null; // 获取发送消息时所关联的数据
            if (correlationData != null) {
                id = correlationData.getId();
            }
            log.info("消息id：{}", id);

            if (ack) {
                log.info("消息成功到达交换机");
                return;
            }
            log.error("消息没有到达交换机，原因为：{}", cause);
        });
    }

    public void sendMessage() {
        // 创建消息
        Message message = MessageBuilder.withBody("订单消息1111".getBytes()).build();
        // 发送消息
        CorrelationData correlationData = new CorrelationData(); // 关联数据
        correlationData.setId("6666666"); // 订单id
        rabbitTemplate.convertAndSend("exchange.confirm.1", "info", message, correlationData);
        log.info("发送消息完毕，发送时间：{}", new Date());
    }
}
