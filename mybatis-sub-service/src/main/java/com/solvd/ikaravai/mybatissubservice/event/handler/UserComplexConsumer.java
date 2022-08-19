package com.solvd.ikaravai.mybatissubservice.event.handler;

import com.solvd.ikaravai.mybatissubservice.config.RabbitComplexOutputProperties;
import com.solvd.ikaravai.mybatissubservice.event.model.MessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserComplexConsumer {

    private final RabbitTemplate template;
    private final RabbitComplexOutputProperties complexOutputProperties;

    @RabbitListener(queues = "fanout_queue1")
    public void receiveMessageFromFanout1(MessagePayload messagePayload) {
        messagePayload.setMessage("Mybatis-SUB-service, receiveMessageFromFanout1");
        messagePayload.setVersion(messagePayload.getVersion() + 1);
        log.info("Hi from myb-SUB, FANOUT-1- : {}", messagePayload);
        messagePayload.setMessage("CASUAL MSG : " + messagePayload.getMessage());
        template.convertAndSend(complexOutputProperties.getExchange(), "some.casual.stuff", messagePayload);
    }

    @RabbitListener(queues = "fanout_queue2")
    public void receiveMessageFromFanout2(MessagePayload messagePayload) {
        messagePayload.setMessage("Mybatis-SUB-service, receiveMessageFromFanout2");
        messagePayload.setVersion(messagePayload.getVersion() + 1);
        log.info("Hi from myb-SUB, FANOUT-2- : {}", messagePayload);
        messagePayload.setMessage("IMPORTANT MSG : " + messagePayload.getMessage());
        template.convertAndSend(complexOutputProperties.getExchange(), "some.important.dogshit", messagePayload);
    }
}
