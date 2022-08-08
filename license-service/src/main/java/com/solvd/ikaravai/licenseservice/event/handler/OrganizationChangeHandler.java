package com.solvd.ikaravai.licenseservice.event.handler;

import com.solvd.ikaravai.licenseservice.event.CustomChannel;
import com.solvd.ikaravai.licenseservice.event.model.OrganizationChangeModel;
import com.solvd.ikaravai.licenseservice.repository.OrganizationRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

@EnableBinding(CustomChannel.class)
@Log4j2
@RequiredArgsConstructor
public class OrganizationChangeHandler {

    private final OrganizationRedisRepository organizationRedisRepository;

    @StreamListener("inboundOrgChanges")
    public void loggerSink(OrganizationChangeModel changeModel) {
        log.info("Received a message of type {}", changeModel.getType());
        log.info("Received a message with an event {} from the organization service for the org id {} with corr Id {}",
                changeModel.getAction(), changeModel.getOrganizationId(), changeModel.getCorrelationId());

    }
}
