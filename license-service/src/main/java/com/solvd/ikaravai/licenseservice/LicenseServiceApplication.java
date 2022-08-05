package com.solvd.ikaravai.licenseservice;

import com.solvd.ikaravai.licenseservice.config.RedisConfig;
import com.solvd.ikaravai.licenseservice.config.ServiceConfig;
import com.solvd.ikaravai.licenseservice.event.model.OrganizationChangeModel;
import com.solvd.ikaravai.licenseservice.model.Organization;
import com.solvd.ikaravai.licenseservice.utils.UserContextInterceptor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

@SpringBootApplication
@RefreshScope
//@EnableDiscoveryClient
@EnableFeignClients
@EnableBinding(Sink.class)
@Log4j2
public class LicenseServiceApplication {

    @Autowired
    private RedisConfig redisConfig;

    public static void main(String[] args) {
        SpringApplication.run(LicenseServiceApplication.class, args);
    }

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory() {
        String hostname = redisConfig.getRedisServer();
        int port = Integer.parseInt(redisConfig.getRedisPort());
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostname, port);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<String, Organization> redisTemplate() {
        RedisTemplate<String, Organization> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory());
        return template;
    }

    @StreamListener(Sink.INPUT)
    public void loggerSink(OrganizationChangeModel change) {
        log.info("Received an {} event for organization ID: {}", change.getAction(), change.getOrganizationId());
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
        localeResolver.setDefaultLocale(Locale.US);
        return localeResolver;
    }

    @Bean
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setBasenames("messages");
        return messageSource;
    }

    @Bean
    @LoadBalanced
    public RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (interceptors == null) {
            restTemplate.setInterceptors(
                    Collections.singletonList(new UserContextInterceptor())
            );
        } else {
            interceptors.add(new UserContextInterceptor());
            restTemplate.setInterceptors(interceptors);
        }
        return restTemplate;
    }

//    @Bean
//    JedisConnectionFactory jedisConnectionFactory() {
//        String hostname = redisConfig.getRedisServer();
//        int port = Integer.parseInt(redisConfig.getRedisPort());
//        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(hostname, port);
//        return new JedisConnectionFactory(redisStandaloneConfiguration);
//    }
}
