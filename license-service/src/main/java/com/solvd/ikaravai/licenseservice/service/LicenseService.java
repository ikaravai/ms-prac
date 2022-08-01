package com.solvd.ikaravai.licenseservice.service;

import com.solvd.ikaravai.licenseservice.config.ServiceConfig;
import com.solvd.ikaravai.licenseservice.model.License;
import com.solvd.ikaravai.licenseservice.model.Organization;
import com.solvd.ikaravai.licenseservice.repository.LicenseRepository;
import com.solvd.ikaravai.licenseservice.service.client.OrganizationDiscoveryClient;
import com.solvd.ikaravai.licenseservice.service.client.OrganizationFeignClient;
import com.solvd.ikaravai.licenseservice.service.client.OrganizationRestTemplateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class LicenseService {

    private final MessageSource messages;
    private final LicenseRepository licenseRepository;
    private final ServiceConfig config;
    private final OrganizationDiscoveryClient organizationDiscoveryClient;
    private final OrganizationFeignClient organizationFeignClient;
    private final OrganizationRestTemplateClient organizationRestTemplateClient;

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
                organization = organizationRestTemplateClient.getOrganization(organizationId);
            }
            case "discovery" -> {
                log.info("I am using the discovery client");
                organization = organizationDiscoveryClient.getOrganization(organizationId);
            }
            default -> {
                log.info("I am using the rest client - DEFAULT");
                organization = organizationRestTemplateClient.getOrganization(organizationId);
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
