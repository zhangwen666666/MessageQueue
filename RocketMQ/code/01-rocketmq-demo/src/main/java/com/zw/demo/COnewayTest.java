package com.zw.demo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

public class COnewayTest {
    @Test
    public void onewayProducer() throws Exception {
        // 1. 创建一个生产者
        DefaultMQProducer producer = new DefaultMQProducer("oneway-producer-group");
        // 2. 连接NameServer
        producer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 启动生产者
        producer.start();
        // 4. 创建一个消息
        Message message = new Message("onewayTopic", "我发送了一个单向消息".getBytes());
        // 5. 发送单向消息
        producer.sendOneway(message);
        // 6. 关闭生产者
        producer.shutdown();
    }
}
