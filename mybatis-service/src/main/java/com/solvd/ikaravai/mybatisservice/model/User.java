package com.solvd.ikaravai.mybatisservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private Long id;
    private String email;
    private String name;
    private Address address;
}
