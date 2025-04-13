package com.zw.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zw.vo.Orders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

@Service
@Slf4j
public class MessageService {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${my.exchangeName}")
    private String exchangeName;
    @Autowired
    private ObjectMapper objectMapper;

    public void sendMessage() throws JsonProcessingException {
        {
            Orders order1 = Orders.builder()
                    .orderId("order_666")
                    .orderName("手机订单")
                    .orderTime(new Date())
                    .orderPrice(new BigDecimal(6666)).build();
            String json1 = objectMapper.writeValueAsString(order1); // 转换为json串
            Message message = MessageBuilder.withBody(json1.getBytes()).build();
            rabbitTemplate.convertAndSend(exchangeName, "info", message);
            log.info("发送消息完毕，发送时间：{}", new Date());
        }
        {
            Orders order2 = Orders.builder()
                    .orderId("order_666")
                    .orderName("手机订单")
                    .orderTime(new Date())
                    .orderPrice(new BigDecimal(6666)).build();
            String json2 = objectMapper.writeValueAsString(order2); // 转换为json串

            // 创建消息
            Message message = MessageBuilder.withBody(json2.getBytes()).build();
            // 发送消息
            rabbitTemplate.convertAndSend(exchangeName, "info", message);
            log.info("发送消息完毕，发送时间：{}", new Date());
        }
    }
}
