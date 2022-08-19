package com.solvd.ikaravai.mybatissubservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq.out.complex.topic")
@Getter @Setter
public class RabbitComplexOutputProperties {

    private String queue1;
    private String queue2;
    private String exchange;
    private String pattern1;
    private String pattern2;
}
