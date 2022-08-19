package com.solvd.ikaravai.mybatisservice.event.handler;

import com.solvd.ikaravai.mybatisservice.config.RabbitSimpleOutputProperties;
import com.solvd.ikaravai.mybatisservice.event.model.MessagePayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserSimpleConsumer {

    private final RabbitTemplate template;
    private final RabbitSimpleOutputProperties simpleOutputProperties;

    @RabbitListener(queues = "save_user_resp_q")
    public void consumeMessageFromQueue(MessagePayload message) {
        log.info("Msg consumed from q : {}", message);
        if (message.getVersion() <= 5) {
            message.setVersion(message.getVersion() + 1);
            log.info("Message sent from Mybatis-Service consumeMessageFromQueue() with v.{}, Time : {}", message.getVersion(), LocalTime.now());
            template.convertAndSend(simpleOutputProperties.getExchange(), simpleOutputProperties.getRoutingKey(), message);
        }
    }
}
