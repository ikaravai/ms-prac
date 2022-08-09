package com.solvd.ikaravai.licenseservice.service.client;

import com.solvd.ikaravai.licenseservice.model.Organization;
import com.solvd.ikaravai.licenseservice.repository.OrganizationRedisRepository;
import com.solvd.ikaravai.licenseservice.utils.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrganizationRestTemplateClient {

    private final KeycloakRestTemplate restTemplate;
    private final OrganizationRedisRepository organizationRedisRepository;

    public Organization getOrganization(String organizationId) {
        log.info("Im license service.getOrganization: {}", UserContext.getCorrelationId());
        Organization organization = checkRedisCache(organizationId);
        if (organization != null) {
            log.info("I have successfully retrieved an organization {} from the redis cache: {}", organizationId, organization);
            return organization;
        }
        log.info("Unable to locate organization from the redis cache: {}", organizationId);
        ResponseEntity<Organization> restExchange = restTemplate.exchange(
                "http://my-gateway-service:8072/organization/v1/organization/{organizationId}",
                HttpMethod.GET,
                null,
                Organization.class,
                organizationId
        );
        organization = restExchange.getBody();
        if (organization != null) {
            cacheOrganizationObject(organization);
        }
        return restExchange.getBody();
    }

    private Organization checkRedisCache(String organizationId) {
        try {
            return organizationRedisRepository.findById(organizationId).orElse(null);
        } catch (Exception ex){
            log.error("Error encountered while trying to retrieve organization {} check Redis Cache.  Exception {}", organizationId, ex);
            return null;
        }
    }

    private void cacheOrganizationObject(Organization organization) {
        try {
            organizationRedisRepository.save(organization);
        } catch (Exception ex){
            log.error("Unable to cache organization {} in Redis. Exception {}", organization.getId(), ex);
        }
    }
}
