package com.solvd.ikaravai.mybatisservice.event.model;

import com.solvd.ikaravai.mybatisservice.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MessagePayload {

    private String message;
    private Integer version;
    private User user;
}
