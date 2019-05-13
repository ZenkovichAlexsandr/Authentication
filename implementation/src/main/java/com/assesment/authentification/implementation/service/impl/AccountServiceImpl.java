package com.assesment.authentification.implementation.service.impl;

import com.assesment.authentification.implementation.model.Account;
import com.assesment.authentification.implementation.model.User;
import com.assesment.authentification.implementation.repository.AccountRepository;
import com.assesment.authentification.implementation.repository.UserRepository;
import com.assesment.authentification.implementation.service.AccountService;
import com.assesment.authentification.implementation.service.dto.AccountDto;
import com.assesment.authentification.implementation.service.dto.NewAccountDto;
import com.assesment.authentification.implementation.service.dto.NewAccountResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class AccountServiceImpl implements AccountService {
    @Value("${account-api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public NewAccountResponseDto create(final NewAccountDto dto) {
        final AccountDto account = findAccount(dto.getAccountNumber());

        final User newUser = createNewUser(account.getOwnerId(), dto);
        final Account newAccount = new Account(account.getIban(), newUser, dto.getAccountNumber());
        accountRepository.save(newAccount);

        return NewAccountResponseDto.builder()
                .username(newUser.getUsername())
                .iban(newAccount.getIban())
                .accountNumber(newAccount.getAccountNumber())
                .build();
    }

    private User createNewUser(final String ownerId, final NewAccountDto dto) {
        return userRepository.save(new User(ownerId, dto.getUsername(), passwordEncoder.encode(dto.getPassword())));
    }

    private AccountDto findAccount(final String accountNumber) {
        try {
            return restTemplate
                    .getForEntity(getUrl(accountNumber), AccountDto.class)
                    .getBody();
        } catch (HttpClientErrorException ex) {
            throw new IllegalArgumentException("Account number do not exists in Account API - " + accountNumber);
        }
    }

    private String getUrl(final String accountNumber) {
        return apiUrl + accountNumber;
    }
}
