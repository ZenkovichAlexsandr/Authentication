package com.assesment.authentification.implementation.service;

import com.assesment.authentification.implementation.service.dto.LoginRequestDto;
import com.assesment.authentification.implementation.service.dto.LoginResponseDto;

public interface AuthenticationService {
    /**
     * Login user.
     *
     * @param dto - used login and password {@link LoginRequestDto}
     * @return generated token {@link LoginResponseDto}
     */
    LoginResponseDto login(LoginRequestDto dto);
}
