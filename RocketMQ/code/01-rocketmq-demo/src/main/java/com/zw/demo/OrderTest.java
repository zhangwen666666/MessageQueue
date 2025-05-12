package com.zw.demo;

import com.zw.domain.Order;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class OrderTest {
    @Test
    public void orderProducer() throws Exception {
        // 1.创建生产者
        DefaultMQProducer producer = new DefaultMQProducer("order-producer-group");
        // 2.连接NameServer
        producer.setNamesrvAddr("111.119.211.126:9876");
        // 3.启动生产者
        producer.start();

        // 创建订单信息
        List<Order> orders = Arrays.asList(
            new Order("111", 1, "下单"),
            new Order("111", 1, "物流"),
            new Order("111", 1, "签收"),
            new Order("222", 2, "下单"),
            new Order("222", 2, "物流"),
            new Order("222", 2, "拒收")
        );

        for (Order order : orders) {
            // 4. 创建消息
            Message message = new Message("orderTopic", order.toString().getBytes());
            // 5. 发送消息
            producer.send(message,  new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> list, Message message, Object arg) {
                    // 这里arg就是send方法的第三个参数，即订单编号
                    // 我们要在这个方法中进行选择队列，让相同的订单号去同一个消息队列中。
                    int hashCode = arg.toString().hashCode();
                    int index = hashCode % list.size(); // 求余数，求模 获取一个队列的下标
                    return list.get(index);
                }
            }, order.getOrderSn());
        }
        System.out.println("发送消息成功");
        // 6.关闭生产者
        producer.shutdown();
    }

    @Test
    public void orderConsumer() throws Exception {
        // 1. 创建一个消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("order-consumer-group");
        // 2. 连接NameServer
        consumer.setNamesrvAddr("111.119.211.126:9876");
        // 3. 订阅主题
        consumer.subscribe("orderTopic", "*");
        // 4. 设置一个监听器 (异步进行的，一直监听)
        // 这里我们不能使用 MessageListenerConcurrently，因为这个监听器是并发的，不能保证消息的顺序。
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                System.out.println("线程id:" + Thread.currentThread().getId() + ", 接收到消息：" + new String(list.get(0).getBody()));
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        // 5. 启动消费者
        consumer.start();
        System.in.read();
    }
}
