package com.zw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

/**
 * ConfirmCallback 接口是一个函数式接口，只有一个抽象的confirm方法
 */
@Component
@Slf4j
public class MyConfirmCallback implements RabbitTemplate.ConfirmCallback {
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//        String id = null; // 获取发送消息时所关联的数据
//        if (correlationData != null) {
//            id = correlationData.getId();
//        }
//        log.info("消息id：{}", id);
//
//        if (ack) {
//            log.info("消息成功到达交换机");
//            return;
//        }
//        log.error("消息没有到达交换机，原因为：{}", cause);
    }
}
