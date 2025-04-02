package com.zw;

import com.zw.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 实现ApplicationRunner接口，在项目启动后，执行run方法
public class Application implements ApplicationRunner {

    @Autowired
    private MessageService messageService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    /**
     * 启动任务，程序一启动就会执行
     * @param args
     * @throws Exception
     */
    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 发送消息，消息先到扇形交换机exchange.fanout
        // 然后转发给与它相连的所有消息队列queue.fanout.a和queue.fanout.b
        messageService.sendMessage();
    }
}
