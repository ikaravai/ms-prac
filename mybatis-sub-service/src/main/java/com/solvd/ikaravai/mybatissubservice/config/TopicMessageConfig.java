package com.solvd.ikaravai.mybatissubservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TopicMessageConfig {

    private final RabbitComplexOutputProperties complexOutputProperties;

    @Bean
    public Declarables topicBindings() {
        Queue topicQueue1 = new Queue(complexOutputProperties.getQueue1(), true);
        Queue topicQueue2 = new Queue(complexOutputProperties.getQueue2(), true);
        TopicExchange topicExchange = new TopicExchange(complexOutputProperties.getExchange());
        return new Declarables(
                topicQueue1,
                topicQueue2,
                topicExchange,
                BindingBuilder.bind(topicQueue1).to(topicExchange).with(complexOutputProperties.getPattern1()),
                BindingBuilder.bind(topicQueue2).to(topicExchange).with(complexOutputProperties.getPattern2())
        );
    }
}
