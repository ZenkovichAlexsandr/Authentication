package com.assesment.authentification.implementation.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthUserDto implements Dto {
    private long id;
    private String username;
    private String role;
}
