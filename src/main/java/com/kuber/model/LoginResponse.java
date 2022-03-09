package com.kuber.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
public class LoginResponse {

    private String token;

    private String type = "Bearer";

    private int userId;

    private String userName;

    private String email;

    private List<String> roles;
}
