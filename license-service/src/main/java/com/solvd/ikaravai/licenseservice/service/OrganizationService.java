package com.solvd.ikaravai.licenseservice.service;

import com.solvd.ikaravai.licenseservice.service.client.OrganizationRestTemplateClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRestTemplateClient organizationRestTemplateClient;

}
