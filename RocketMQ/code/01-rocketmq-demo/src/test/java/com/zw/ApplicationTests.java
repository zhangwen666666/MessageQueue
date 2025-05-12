package com.zw;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
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




}
