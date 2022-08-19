package com.solvd.ikaravai.mybatisservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "rabbitmq.out.simple")
@Configuration
@Getter @Setter
public class RabbitSimpleOutputProperties {

    private String routingKey;
    private String queue;
    private String exchange;
}
