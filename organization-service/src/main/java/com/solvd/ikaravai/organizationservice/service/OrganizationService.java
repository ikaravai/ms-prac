package com.solvd.ikaravai.organizationservice.service;

import brave.ScopedSpan;
import brave.Tracer;
import com.solvd.ikaravai.organizationservice.event.model.ActionEnum;
import com.solvd.ikaravai.organizationservice.event.source.SimpleSourceBean;
import com.solvd.ikaravai.organizationservice.model.Organization;
import com.solvd.ikaravai.organizationservice.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final SimpleSourceBean simpleSourceBean;
    private final Tracer tracer;

    public Organization findById(String organizationId) {
//        Optional<Organization> org = organizationRepository.findById(organizationId);
//        simpleSourceBean.publishOrganizationChange(ActionEnum.GET, organizationId);
//        return org.orElse(null);
        Optional<Organization> opt = null;
        ScopedSpan newSpan = tracer.startScopedSpan("getOrgDBCall");
        try {
            opt = organizationRepository.findById(organizationId);
            simpleSourceBean.publishOrganizationChange(ActionEnum.GET, organizationId);
            if (!opt.isPresent()) {
                String message = String.format("Unable to find an organization with the Organization id %s", organizationId);
                log.error(message);
                throw new IllegalArgumentException(message);
            }
            log.debug("Retrieving Organization Info: " + opt.get().toString());
        }finally {
            newSpan.tag("peer.service", "postgres");
            newSpan.annotate("Client received");
            newSpan.finish();
        }
        return opt.get();
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
