package com.solvd.ikaravai.organizationservice.service;

import com.solvd.ikaravai.organizationservice.event.model.ActionEnum;
import com.solvd.ikaravai.organizationservice.event.source.SimpleSourceBean;
import com.solvd.ikaravai.organizationservice.model.Organization;
import com.solvd.ikaravai.organizationservice.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final SimpleSourceBean simpleSourceBean;

    public Organization findById(String organizationId) {
        Optional<Organization> org = organizationRepository.findById(organizationId);
        simpleSourceBean.publishOrganizationChange(ActionEnum.GET, organizationId);
        return org.orElse(null);
    }

    public Organization create(Organization organization){
        organization.setId(UUID.randomUUID().toString());
        organization = organizationRepository.save(organization);
        simpleSourceBean.publishOrganizationChange(ActionEnum.CREATED, organization.getId());
        return organization;
    }

    public void update(Organization organization){
        organizationRepository.save(organization);
        simpleSourceBean.publishOrganizationChange(ActionEnum.UPDATED, organization.getId());
    }

    public void delete(Organization organization){
        organizationRepository.deleteById(organization.getId());
        simpleSourceBean.publishOrganizationChange(ActionEnum.DELETED, organization.getId());
    }
}
