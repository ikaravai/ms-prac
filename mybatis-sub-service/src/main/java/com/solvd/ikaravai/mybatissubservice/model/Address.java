package com.solvd.ikaravai.mybatissubservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class Address {

    private Long userId;
    private String city;
    private String street;
    private Integer houseNumber;
    private Integer index;
}
