package com.assesment.authentification.implementation.service;

import com.assesment.authentification.implementation.service.dto.NewAccountDto;
import com.assesment.authentification.implementation.service.dto.NewAccountResponseDto;

public interface AccountService {
    /**
     * Creates new user account.
     * User can bi created just if he has account in Account API.
     *
     * @param dto - new used info {@link NewAccountDto}
     * @return information about new account {@link NewAccountResponseDto}
     */
    NewAccountResponseDto create(NewAccountDto dto);
}
