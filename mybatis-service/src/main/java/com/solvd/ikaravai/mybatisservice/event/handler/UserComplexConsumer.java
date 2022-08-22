package com.solvd.ikaravai.mybatisservice.event.handler;

import com.solvd.ikaravai.mybatisservice.event.model.MessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UserComplexConsumer {

    @RabbitListener(queues = "topic_queue1")
    public void receiveMessageFromFanout1(MessagePayload messagePayload) {
        log.info("Hi from MYBATIS, [IMPORTANT] TOPIC-1- : {}", messagePayload);
    }

    @RabbitListener(queues = "topic_queue2")
    public void receiveMessageFromFanout2(MessagePayload messagePayload) {
        log.info("Hi from MYBATIS, [CASUAL] TOPIC-2- : {}", messagePayload);
    }
}
