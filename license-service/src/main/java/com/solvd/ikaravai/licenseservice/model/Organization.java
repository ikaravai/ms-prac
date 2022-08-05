package com.solvd.ikaravai.licenseservice.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Id;
import java.io.Serializable;

@Getter @Setter @ToString
//@EqualsAndHashCode(callSuper = true)
@RedisHash("organization")
//public class Organization extends RepresentationModel<Organization> {
public class Organization implements Serializable {
//public class Organization {

    @Id
    String id;
    String name;
    String contactName;
    String contactEmail;
    String contactPhone;
}
