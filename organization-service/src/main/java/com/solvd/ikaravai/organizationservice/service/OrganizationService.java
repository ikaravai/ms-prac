package com.solvd.ikaravai.organizationservice.service;

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

    public Organization findById(String organizationId) {
        Optional<Organization> org = organizationRepository.findById(organizationId);
        return org.orElse(null);
    }

    public Organization create(Organization organization){
        organization.setId(UUID.randomUUID().toString());
        organization = organizationRepository.save(organization);
        return organization;
    }

    public void update(Organization organization){
        organizationRepository.save(organization);
    }

    public void delete(Organization organization){
        organizationRepository.deleteById(organization.getId());
    }
}
