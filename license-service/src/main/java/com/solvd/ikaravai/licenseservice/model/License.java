package com.solvd.ikaravai.licenseservice.model;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
@ToString
@EqualsAndHashCode(callSuper = true)
@Builder
public class License extends RepresentationModel<License> {

    private Long id;
    private String licenseId;
    private String description;
    private String organizationId;
    private String productName;
    private String licenseType;
}
