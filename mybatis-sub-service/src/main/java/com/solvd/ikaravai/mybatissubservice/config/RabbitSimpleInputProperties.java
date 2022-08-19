package com.solvd.ikaravai.mybatissubservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "rabbitmq.in.simple")
@Configuration
@Getter @Setter
public class RabbitSimpleInputProperties {

    private String routingKey;
    private String queue;
    private String exchange;
}
