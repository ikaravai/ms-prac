package com.solvd.ikaravai.mybatisservice.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "rabbitmq.out.complex.fanout")
@Getter @Setter
public class RabbitComplexOutputProperties {

    private String queue1;
    private String queue2;
    private String exchange;
}
