package com.solvd.ikaravai.licenseservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@AllArgsConstructor
public class OrganizationChangeModel {

    private String type;
    private String action;
    private String organizationId;
    private String correlationId;

}
