package com.assesment.authentification.implementation.controller;

import com.assesment.authentification.implementation.service.AuthenticationService;
import com.assesment.authentification.implementation.service.dto.LoginRequestDto;
import com.assesment.authentification.implementation.service.dto.LoginResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    /**
     * Login user.
     *
     * @param dto - used login and password {@link LoginRequestDto}
     * @return generated token {@link LoginResponseDto}
     */
    @PostMapping(value = "/login")
    @ResponseStatus(value = HttpStatus.OK)
    public LoginResponseDto login(@RequestBody final LoginRequestDto dto) {
        return service.login(dto);
    }
}
