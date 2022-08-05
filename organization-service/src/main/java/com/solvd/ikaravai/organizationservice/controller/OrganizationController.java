package com.solvd.ikaravai.organizationservice.controller;

import com.solvd.ikaravai.organizationservice.model.Organization;
import com.solvd.ikaravai.organizationservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping("/{organizationId}")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Organization> getOrganization(@PathVariable("organizationId") String organizationId) {
        return ResponseEntity.ok(organizationService.findById(organizationId));
    }

    @PutMapping("/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateOrganization( @PathVariable("organizationId") String id, @RequestBody Organization organization) {
        organizationService.update(organization);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Organization>  saveOrganization(@RequestBody Organization organization) {
        return ResponseEntity.ok(organizationService.create(organization));
    }

    @DeleteMapping("/{organizationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization( @PathVariable("id") String id,  @RequestBody Organization organization) {
        organizationService.delete(organization);
    }
}
