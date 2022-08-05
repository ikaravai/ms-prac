package com.solvd.ikaravai.licenseservice.repository;

import com.solvd.ikaravai.licenseservice.model.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRedisRepository extends CrudRepository<Organization, String> {

}
