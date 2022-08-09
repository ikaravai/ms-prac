package com.solvd.ikaravai.licenseservice.service;

import com.solvd.ikaravai.licenseservice.config.ServiceConfig;
import com.solvd.ikaravai.licenseservice.model.License;
import com.solvd.ikaravai.licenseservice.model.Organization;
import com.solvd.ikaravai.licenseservice.repository.LicenseRepository;
import com.solvd.ikaravai.licenseservice.service.client.OrganizationDiscoveryClient;
import com.solvd.ikaravai.licenseservice.service.client.OrganizationFeignClient;
import com.solvd.ikaravai.licenseservice.service.client.OrganizationRestTemplateClient;
import com.solvd.ikaravai.licenseservice.utils.UserContextHolder;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
@Slf4j
public class LicenseService {

    private final MessageSource messages;
    private final LicenseRepository licenseRepository;
    private final ServiceConfig config;
    private final OrganizationDiscoveryClient organizationDiscoveryClient;
    private final OrganizationFeignClient organizationFeignClient;
    private final OrganizationRestTemplateClient organizationRestTemplateClient;

    @CircuitBreaker(name = "licenseService"
//            , fallbackMethod = "getLicensesByOrganizationFallback"
    )
    @Bulkhead(name = "bulkheadLicenseService"
//            , fallbackMethod = "getLicensesByOrganizationFallback"
//            ,
//            type = Bulkhead.Type.SEMAPHORE
    )
    @Retry(name = "retryLicenseService"
            , fallbackMethod = "getLicensesByOrganizationFallback"
    )
    @RateLimiter(name = "limiterLicenseService", fallbackMethod = "getLicensesByOrganizationFallback")
    public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
        log.info("getLicensesByOrganization Correlation id: {}",
                UserContextHolder.getContext().getCorrelationId());
        randomlyRunLong();
        return licenseRepository.findByOrganizationId(organizationId);
    }

    public List<License> getLicensesByOrganizationFallback(String organizationId, Throwable t) throws TimeoutException {
        List<License> fallbackList = new ArrayList<>();
        License license = new License();
        license.setLicenseId("0000-0000-0001");
        license.setOrganizationId(organizationId);
        license.setProductName("Sry, not available :(");
        fallbackList.add(license);
        return fallbackList;
    }

    @CircuitBreaker(name = "organizationService")
    private Organization getOrganization(String organizationId) {
        return organizationRestTemplateClient.getOrganization(organizationId);
    }

    private void randomlyRunLong() throws TimeoutException {
        Random rand = new Random();
        int randomNum = rand.nextInt((2 - 1) + 1) + 1;
        if (randomNum == 2) sleep();
    }
    private void sleep() throws TimeoutException{
        try {
            log.info("Sleep");
            Thread.sleep(500);
            throw new java.util.concurrent.TimeoutException("Timeout");
        } catch (InterruptedException e) {
            log.error(e.getMessage());
        }
    }

    public License getLicense(String licenseId, String organizationId, String clientType) {
        License license = licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
        if (null == license) {
            throw new IllegalArgumentException(String.format(messages.getMessage("license.search.error.message", null, null),licenseId, organizationId));
        }
        Organization organization = retrieveOrganizationInfo(organizationId, clientType);
        if (null != organization) {
            license.setOrganizationName(organization.getName());
            license.setContactName(organization.getContactName());
            license.setContactEmail(organization.getContactEmail());
            license.setContactPhone(organization.getContactPhone());
        }
        return license.withComment(config.getProperty());
    }

    private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
        Organization organization = null;
        switch (clientType) {
            case "feign" -> {
                log.info("I am using the feign client");
                organization = organizationFeignClient.getOrganization(organizationId);
            }
            case "rest" -> {
                log.info("I am using the rest client");
                organization = getOrganization(organizationId);
            }
            case "discovery" -> {
                log.info("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
            }
            default -> {
                log.info("I am using the rest client - DEFAULT");
                organization = getOrganization(organizationId);
            }
        }
        return organization;
    }

    public License createLicense(License license){
        license.setLicenseId(UUID.randomUUID().toString());
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public License updateLicense(License license){
        licenseRepository.save(license);
        return license.withComment(config.getProperty());
    }

    public String deleteLicense(String licenseId){
        String responseMessage = null;
        License license = new License();
        license.setLicenseId(licenseId);
        licenseRepository.delete(license);
        responseMessage = String.format(messages.getMessage("license.delete.message", null, null),licenseId);
        return responseMessage;

    }
}
