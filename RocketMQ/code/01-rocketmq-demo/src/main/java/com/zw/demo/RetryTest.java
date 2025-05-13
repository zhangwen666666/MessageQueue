package com.zw.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;

public class RetryTest {
    @Test
    public void retryProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("retry-producer-group");
        producer.setNamesrvAddr("111.119.211.126:9876");
        producer.start();
        //设置重试次数
        producer.setRetryTimesWhenSendFailed(2); // 同步发送失败时重复2次
        producer.setRetryTimesWhenSendAsyncFailed(2); // 异步发送失败时重复2次
        String key = UUID.randomUUID().toString();
        System.out.println(key);
        Message message = new Message("retryTopic", "vip", key, "88888".getBytes());
        producer.send(message);
        producer.shutdown();
    }

    @Test
    public void retryConsumer() throws Exception {
        // 1. 创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-consumer-group");
        // 2. 连接NameServer
        consumer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 订阅主题和标签
        consumer.subscribe("retryTopic", "vip"); //只订阅vip

        consumer.setMaxReconsumeTimes(2);
        // 4. 注册监听器
        consumer.registerMessageListener((MessageListenerConcurrently) (list, contest) -> {
            System.out.println(new Date());
            String msg = new String(list.get(0).getBody());
            System.out.println("我是vip，我正在消费消息：" + msg);
            // 处理业务
            try {
                handleDB();
            } catch (Exception e) {
                // 事务回滚等操作
                // 我们定义了最大重试次数是2
                // 第二次重试还失败的话，则进入死信队列，
                if (list.get(0).getReconsumeTimes() >= 2) {
                    // 我们在这里签收记录消息，通知人工处理
                    System.out.println("消息" + msg + "已签收，等待人工处理");
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
                // 否则就需要重试
                return ConsumeConcurrentlyStatus.RECONSUME_LATER;
            }
            // 能走到这里说明业务没有报错，正常签收消息。
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 5. 启动消费者
        consumer.start();
        System.in.read();
    }

    private void handleDB() {
        // 假设此方法就是xxxService类中的业务处理方法
        // 模拟一个错误
        int i = 1 / 0;
    }


    @Test
    public void retryDeadConsumer() throws Exception {
        // 1. 创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("retry-dead-consumer-group");
        // 2. 连接NameServer
        consumer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 订阅主题和标签
        consumer.subscribe("%DLQ%retry-consumer-group", "*"); //
        // 4. 注册监听器
        consumer.registerMessageListener((MessageListenerConcurrently) (list, contest) -> {
            System.out.println(new Date());
            System.out.println("我是死信队列，我正在消费消息：" + new String(list.get(0).getBody()));
            // 这里可以将此消息记录下来，进行人工处理
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });
        // 5. 启动消费者
        consumer.start();
        System.in.read();
    }
}
