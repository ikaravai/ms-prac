package com.solvd.ikaravai.licenseservice.service;

import com.solvd.ikaravai.licenseservice.service.client.OrganizationRestTemplateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRestTemplateClient organizationRestTemplateClient;

}
