package com.zw.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

import java.util.UUID;

public class KeyTest {
    @Test
    public void keyProducer() throws Exception {
        // 1. 创建一个生产者
        DefaultMQProducer producer = new DefaultMQProducer("key-producer-group");
        // 2.连接NameServer
        producer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 启动生产者
        producer.start();
        // 4. 创建一个带标签tag和key的消息
        String key = UUID.randomUUID().toString(); // 生成消息的key
        System.out.println(key);
        Message message = new Message("keyTopic", "vip", key, "我一个vip的消息".getBytes());
        // 5. 发送消息
        producer.send(message);
        System.out.println("发送完成");
        // 6. 关闭生产者
        producer.shutdown();
    }

    @Test
    public void keyConsumer() throws Exception{
        // 1. 创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("key-consumer-group");
        // 2. 连接NameServer
        consumer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 订阅主题和标签
        consumer.subscribe("keyTopic", "vip"); //只订阅vip
        // 4. 注册监听器
        consumer.registerMessageListener((MessageListenerConcurrently) (list, contest) -> {
            System.out.println("我是vip，我正在消费消息：" + new String(list.get(0).getBody()));
            System.out.println(list.get(0).getKeys()); // 获取消息的key
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 5. 启动消费者
        consumer.start();
        System.in.read();
    }
}
