package com.kuber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private int userId;

    private String userName;

    private String email;

    private String password;

    private List<Role> roles;

}
