package com.zw;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;

@SpringBootTest
class ApplicationTests {

    @Test
    void contextLoads() {
    }

    /*
        // Message(String topic, String tags, String keys, int flag, byte[] body, boolean waitStoreMsgOK)
        // topic：消息所属的Topic，需要提前在NameServer创建
        // tags：消息的标签，可以理解为消息的分类
        // keys：消息的Key，用于消息去重
        // flag：消息的标识，用于消息去重
        // body：消息的内容
        // waitStoreMsgOK：是否等待消息存储到NameServer
     */

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void repeatProducer() throws Exception {
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
    public void repeatConsumer() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("repeat-consumer-group");
        consumer.setNamesrvAddr("111.119.211.126:9876");
        consumer.subscribe("repeatTopic", "*");
        consumer.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
            // 先获取消息的key
            String keys = list.get(0).getKeys();
            String sql = "insert into order_oper_log(`type`, `order_sn`, `userId`) values (1, ?, 6)";
            try {
                jdbcTemplate.update(sql, keys);
            } catch (DuplicateKeyException e) {
                // 捕捉到DuplicateKeyException异常，说明执行sql失败了
                System.out.println("消息已成功消费");
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }

            // 处理业务逻辑
            System.out.println("接收到消息：" + new String(list.get(0).getBody()) + "，keys：" + keys);
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer.start();
        System.in.read();
    }

}
