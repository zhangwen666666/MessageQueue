package com.zw.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

public class TagTest {
    @Test
    public void tagProducer() throws Exception{
        // 1. 创建一个生产者
        DefaultMQProducer producer = new DefaultMQProducer("tag-producer-group");
        // 2.连接NameServer
        producer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 启动生产者
        producer.start();
        // 4. 创建一个带标签tag的消息
        Message message1 = new Message("tagTopic", "vip", "我一个vip的消息".getBytes());
        Message message2 = new Message("tagTopic", "svip", "我一个svip的消息".getBytes());
        // 5. 发送消息
        producer.send(message1);
        producer.send(message2);
        System.out.println("发送完成");
        // 6. 关闭生产者
        producer.shutdown();
    }

    @Test
    public void tagConsumer() throws Exception{
        // 1. 创建消费者
        DefaultMQPushConsumer consumer1 = new DefaultMQPushConsumer("tag-consumer-group-a");
        DefaultMQPushConsumer consumer2 = new DefaultMQPushConsumer("tag-consumer-group-b");
        // 2. 连接NameServer
        consumer1.setNamesrvAddr("111.119.211.126:9876");
        consumer2.setNamesrvAddr("111.119.211.126:9876");
        // 3. 订阅主题和标签
        consumer1.subscribe("tagTopic", "vip"); //只订阅vip
        consumer2.subscribe("tagTopic", "vip || svip"); // 订阅vip和svip
        // 4. 注册监听器
        consumer1.registerMessageListener((MessageListenerConcurrently) (list, contest) -> {
            System.out.println("我是vip，我正在消费消息：" + new String(list.get(0).getBody()));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        consumer2.registerMessageListener((MessageListenerConcurrently) (list, context) -> {
            System.out.println("我是svip，我正在消费消息：" + new String(list.get(0).getBody()));
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 5. 启动消费者
        consumer1.start();
        consumer2.start();
        System.in.read();
    }
}
