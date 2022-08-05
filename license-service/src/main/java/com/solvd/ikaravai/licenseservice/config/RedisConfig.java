package com.solvd.ikaravai.licenseservice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RedisConfig {

    @Value("${redis.server}")
    private String redisServer = "";
    @Value("${redis.port}")
    private String redisPort = "";
}
