package com.zw.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

public class DistinctTest {

    private JdbcTemplate jdbcTemplate;

    @Test
    public void repeatProducer() throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("repeat-producer-group");
        producer.setNamesrvAddr("111.119.211.126:9876");
        producer.start();
        String key = UUID.randomUUID().toString(); // 模拟订单编号
        System.out.println(key);
        // 模拟发送两条一模一样的消息
        Message message1 = new Message("repeatTopic", null, key, "扣减库存-1".getBytes());
        Message message2 = new Message("repeatTopic", null, key, "扣减库存-1".getBytes());
        producer.send(message1);
        producer.send(message2);
        producer.shutdown();
    }

    @Test
    public void repeatConsumer() throws Exception{
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("repeat-consumer-group");
        consumer.setNamesrvAddr("111.119.211.126:9876");
        consumer.subscribe("repeatTopic", "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
            // 先获取消息的key
            String keys = list.get(0).getKeys();
            String sql = "insert into order_oper_log(`type`, `order_sn`, `userId`) values (1, ?, 6)";
            jdbcTemplate.update(sql, keys);
//            if (count <= 0 || )

            // 处理业务逻辑
            System.out.println("接收到消息：" + new String(list.get(0).getBody()));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.in.read();
    }
}
