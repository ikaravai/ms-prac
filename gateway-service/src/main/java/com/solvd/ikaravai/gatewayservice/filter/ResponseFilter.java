package com.solvd.ikaravai.gatewayservice.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class ResponseFilter {

    private final FilterUtils filterUtils;
    private final Tracer tracer;

    @Bean
    public GlobalFilter postGlobalFilter() {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
//            String traceId = tracer.currentSpan().context().traceIdString();
            Span span = exchange.getAttribute(Span.class.getName());
            String traceId = span.context().traceId();
//            HttpHeaders requestHeaders = exchange.getRequest().getHeaders();
//            String correlationId = filterUtils.getCorrelationId(requestHeaders);
//            log.info("Adding the correlation id to the outbound headers. {}", correlationId);
            log.info("Adding the correlation id to the outbound headers. {}", traceId);
            exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, traceId);
            log.info("Completing outgoing request for {}.", exchange.getRequest().getURI());
        }));
    }
}
