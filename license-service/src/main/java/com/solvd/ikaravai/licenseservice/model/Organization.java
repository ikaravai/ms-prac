package com.solvd.ikaravai.licenseservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.hateoas.RepresentationModel;

@Getter @Setter @ToString
@EqualsAndHashCode(callSuper = true)
public class Organization extends RepresentationModel<Organization> {

    String id;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;
}
