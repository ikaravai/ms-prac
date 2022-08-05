package com.solvd.ikaravai.licenseservice.service.client;

import com.solvd.ikaravai.licenseservice.model.Organization;
import lombok.RequiredArgsConstructor;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class OrganizationRestTemplateClient {

    private final KeycloakRestTemplate restTemplate;

    public Organization getOrganization(String organizationId) {
        ResponseEntity<Organization> restExchange = restTemplate.exchange(
                "http://my-gateway-service:8072/organization/v1/organization/{organizationId}",
                HttpMethod.GET,
                null,
                Organization.class,
                organizationId
        );
        return restExchange.getBody();
    }
}
