package com.zw;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    @Test
    void sendMessage() throws Exception {
        // 1. 创建一个生产者 （指定一个组名：test-producer-group）
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
        DefaultMQProducer producer2 = new DefaultMQProducer("test-producer-group");
        // 2. 连接NameServer
        producer.setNamesrvAddr("111.119.211.126:9876");
        producer2.setNamesrvAddr("111.119.211.126:9876");
        // 3. 启动生产者
        producer.start();
        producer2.start();
        // 4. 创建一个消息
        //    这里用的是重载的方法，指定消息主题和内容
        Message message = new Message("testTopic", "我是一个消息666".getBytes());
        Message message2 = new Message("testTopic2", "我是一个消息222".getBytes());
        // 5. 发送消息,发送完成之后会有一个返回值
        SendResult sendResult = producer.send(message, 10000);
        SendResult sendResult2 = producer.send(message2, 10000);
        System.out.println(sendResult);
        System.out.println(sendResult2);
        // 6. 关闭生产者
        producer.shutdown();
        producer2.shutdown();
    }

    @Test
    void consumer() throws Exception{
        // 1. 创建一个消费者 (指定一个组名：test-consumer-group)
//        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        DefaultMQPushConsumer consumer2 = new DefaultMQPushConsumer("test-consumer-group");
        // 2. 连接NameServer
//        consumer.setNamesrvAddr("111.119.211.126:9876");
        consumer2.setNamesrvAddr("111.119.211.126:9876");
        // 3. 订阅主题
        //    testTopic：订阅的主题名称,  *：表示订阅所有标签(所有消息)
//        consumer.subscribe("testTopic", "*");
        consumer2.subscribe("testTopic2", "*");
        // 4. 设置一个监听器 (异步进行的，一直监听)
//        consumer.registerMessageListener(new MessageListenerConcurrently() {
//            /**
//             * 消费消息 这个方法会启动一个新的线程执行，
//             * @param list 消息列表
//             * @param context 并发消息的上下文
//             * @return
//             */
//            @Override
//            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
//                // 这个就是消费的方法
//                MessageExt messageExt = list.get(0);
//                System.out.println(messageExt);
//                System.out.println(new String(messageExt.getBody())); // 消息内容
//                System.out.println(context);
//                // 返回值是ConsumeConcurrentlyStatus,是一个枚举类型
//                // 该枚举类型有两个值: CONSUME_SUCCESS, RECONSUME_LATER;
//                // CONSUME_SUCCESS表示成功，消息会从mq中出队
//                // RECONSUME_LATER表示失败，消息会重新入队,过一会会重新投递
//                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // 消费成功
//            }
//        });
        consumer2.registerMessageListener(new MessageListenerConcurrently() {
            /**
             * 消费消息 这个方法会启动一个新的线程执行，
             * @param list 消息列表
             * @param context 并发消息的上下文
             * @return
             */
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                // 这个就是消费的方法
                MessageExt messageExt = list.get(0);
                System.out.println(messageExt);
                System.out.println(new String(messageExt.getBody())); // 消息内容
                System.out.println(context);
                // 返回值是ConsumeConcurrentlyStatus,是一个枚举类型
                // 该枚举类型有两个值: CONSUME_SUCCESS, RECONSUME_LATER;
                // CONSUME_SUCCESS表示成功，消息会从mq中出队
                // RECONSUME_LATER表示失败，消息会重新入队,过一会会重新投递
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS; // 消费成功
            }
        });
        // 5. 启动消费者
//        consumer.start();
        consumer2.start();

        // 6.挂起当前的jvm
        System.in.read();
    }
}
