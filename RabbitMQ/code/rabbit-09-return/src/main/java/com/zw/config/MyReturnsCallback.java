package com.zw.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyReturnsCallback implements RabbitTemplate.ReturnsCallback{
    /**
     * 消息返回   这个方法只有在消息没有到达消息队列时才会执行
     * @param returnedMessage 这是一个封装了所有关于返回消息信息的对象，包括：
     *      getMessage(): 返回实际的消息。
     *      getReplyCode(): 获取回复码，表示返回的原因。
     *      getReplyText(): 获取回复文本，提供了关于返回原因的详细描述。
     *      getExchange(): 获取消息最初被发布的交换机名称。
     *      getRoutingKey(): 获取消息发布时使用的路由键。
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.error("消息没有正确路由到消息队列,原因为{}",returnedMessage.getReplyText());
    }
}
