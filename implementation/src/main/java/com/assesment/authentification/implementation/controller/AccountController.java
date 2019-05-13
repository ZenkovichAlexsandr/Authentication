package com.assesment.authentification.implementation.controller;

import com.assesment.authentification.implementation.service.AccountService;
import com.assesment.authentification.implementation.service.dto.NewAccountDto;
import com.assesment.authentification.implementation.service.dto.NewAccountResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/account", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountController {
    @Autowired
    private AccountService service;

    /**
     * Creates new user account.
     * User can bi created just if he has account in Account API.
     *
     * @param dto - new used info {@link NewAccountDto}
     * @return information about new account {@link NewAccountResponseDto}
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewAccountResponseDto create(@Valid @RequestBody final NewAccountDto dto) {
        return service.create(dto);
    }
}
