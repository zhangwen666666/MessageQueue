package com.zw.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Date;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${my.exchangeName}")
    private String exchangeName;

    @PostConstruct
    public void init(){
        //开启生产者的确定模式
        rabbitTemplate.setConfirmCallback(
            (correlationData, ack, cause)->{
                if(!ack){
                    log.error("消息没有到达交换机，原因为：{}",cause);
                    //TODO 重发消息或者记录错误日志
                }
            }
        );

        rabbitTemplate.setReturnsCallback(
            returnedMessage->{
                log.error("消息没有从交换机正确的投递（路由）到队列，原因为：{}",returnedMessage.getReplyText());
                //TODO 记录错误日志，给程序员发短信或者或者邮件
            }
        );
    }

    public void sendMessage() {
        // 创建消息
        Message message = MessageBuilder.withBody("hello world".getBytes()).build();
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "info", message);
        log.info("发送消息完毕，发送时间：{}", new Date());
    }
}
