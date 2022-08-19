package com.solvd.ikaravai.mybatissubservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class User {

    private Long id;
    private String email;
    private String name;
    private Address address;
}
