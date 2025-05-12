package com.zw.demo;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

public class AsyncTest {
    @Test
    public void asyncProducer() throws Exception{
        // 1.创建一个生产者
        DefaultMQProducer producer = new DefaultMQProducer("async-producer-group");
        // 2.连接NameServer
        producer.setNamesrvAddr("111.119.211.126:9876");
        // 3.开启生产者
        producer.start();
        // 4.创建一个消息
        Message message = new Message("asyncTopic", "我是一个异步消息".getBytes());
        // 5.发送消息 异步发送，需要设置一个回调函数
        producer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("发送失败:" + throwable.getMessage());
            }
        });
        System.in.read();
    }
}
