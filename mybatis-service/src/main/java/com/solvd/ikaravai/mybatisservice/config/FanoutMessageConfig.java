package com.solvd.ikaravai.mybatisservice.config;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Declarables;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class FanoutMessageConfig {

    private final RabbitComplexOutputProperties complexOutputProperties;

    @Bean
    public Declarables fanoutBindings() {
        Queue fanoutQueue1 = new Queue(complexOutputProperties.getQueue1());
        Queue fanoutQueue2 = new Queue(complexOutputProperties.getQueue2());
        FanoutExchange fanoutExchange = new FanoutExchange(complexOutputProperties.getExchange());
        return new Declarables(
                fanoutQueue1,
                fanoutQueue2,
                fanoutExchange,
                BindingBuilder.bind(fanoutQueue1).to(fanoutExchange),
                BindingBuilder.bind(fanoutQueue2).to(fanoutExchange)
        );
    }
}
