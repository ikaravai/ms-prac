package com.solvd.ikaravai.organizationservice.repository;

import com.solvd.ikaravai.organizationservice.model.Organization;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface OrganizationRepository extends CrudRepository<Organization, String> {

    Optional<Organization> findById(String organizationId);
}
