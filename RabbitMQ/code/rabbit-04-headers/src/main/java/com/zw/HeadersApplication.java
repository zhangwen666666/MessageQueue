package com.zw;

import com.zw.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HeadersApplication implements ApplicationRunner {

    @Autowired
    private MessageService messageService;

    public static void main(String[] args) {
        SpringApplication.run(HeadersApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        messageService.sendMessage();// 发送消息到主题交换机
    }
}
