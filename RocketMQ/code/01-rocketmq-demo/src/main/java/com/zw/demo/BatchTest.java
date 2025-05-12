package com.zw.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class BatchTest {
    @Test
    public void testBatchProducer() throws Exception {
        // 创建默认的生产者
        DefaultMQProducer producer = new DefaultMQProducer("batch-producer-group");
        // 设置nameServer地址
        producer.setNamesrvAddr("111.119.211.126:9876");
        // 启动生产者
        producer.start();
        // 创建一组消息
        List<Message> msgs = Arrays.asList(
            new Message("batchTopic", "我是一组消息的A消息".getBytes()),
            new Message("batchTopic", "我是一组消息的B消息".getBytes()),
            new Message("batchTopic", "我是一组消息的C消息".getBytes())
        );
        // 发送消息
        SendResult send = producer.send(msgs);
        System.out.println(send);
        // 关闭生产者
        producer.shutdown();
    }


    @Test
    public void testBatchConsumer() throws Exception {
        // 创建默认消费者组
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("batch-consumer-group");
        // 设置nameServer地址
        consumer.setNamesrvAddr("111.119.211.126:9876");
        // 订阅一个主题来消费   表达式，默认是*
        consumer.subscribe("batchTopic", "*");
        // 注册一个消费监听 MessageListenerConcurrently是并发消费
        // 默认是20个线程一起消费，可以参看 consumer.setConsumeThreadMax()
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 这里执行消费的代码 默认是多线程消费
                System.out.println(Thread.currentThread().getName() + "----" + new String(msgs.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        consumer.start();
        System.in.read();
    }
}
