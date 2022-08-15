package com.solvd.ikaravai.mybatisservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Address {

    private Long userId;
    private String city;
    private String street;
    private Integer houseNumber;
    private Integer index;
}
