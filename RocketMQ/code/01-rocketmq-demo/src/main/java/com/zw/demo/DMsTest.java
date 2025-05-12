package com.zw.demo;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.Date;
import java.util.List;

public class DMsTest {
    @Test
    public void delayProducer() throws Exception{
        // 1. 创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("delay-producer-group");
        // 2. 连接NameServer
        producer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 开启生产者
        producer.start();
        // 4. 创建消息
        Message message = new Message("delayTopic", "订单号，座位号".getBytes());
        // 5. 给消息设置一个延迟等级
        message.setDelayTimeLevel(3);
        // 6. 发送消息
        // 注意这里的10000不是消息的延迟时间，而是与mq服务连接的超时时间
        producer.send(message, 10000);
        System.out.println("发送消息成功,发送时间：" + new Date());
        // 7. 关闭生产者
        producer.shutdown();
    }

    @Test
    public void delayConsumer() throws Exception{
        // 1. 创建消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("delay-consumer-group");
        // 2. 添加NameServer地址
        consumer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 订阅主题
        consumer.subscribe("delayTopic", "*");
        // 4. 设置监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                System.out.println("收到消息了" + new Date());
                System.out.println(new String(list.get(0).getBody()));
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 5. 启动消费者
        consumer.start();
        System.in.read();
    }
}
