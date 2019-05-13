package com.assesment.authentification.implementation.service;

import com.assesment.authentification.implementation.BaseServiceTest;
import com.assesment.authentification.implementation.model.Account;
import com.assesment.authentification.implementation.model.User;
import com.assesment.authentification.implementation.repository.AccountRepository;
import com.assesment.authentification.implementation.repository.UserRepository;
import com.assesment.authentification.implementation.service.dto.AccountDto;
import com.assesment.authentification.implementation.service.dto.NewAccountDto;
import com.assesment.authentification.implementation.service.dto.NewAccountResponseDto;
import com.assesment.authentification.implementation.service.impl.AccountServiceImpl;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class AccountServiceTest extends BaseServiceTest {
    @Autowired
    private AccountService service;
    @MockBean
    private RestTemplate restTemplate;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @Captor
    private ArgumentCaptor<Account> saveAccountArgumentCaptor;

    @After
    public void after() {
        verifyNoMoreInteractions(
                restTemplate,
                userRepository,
                accountRepository,
                passwordEncoder
        );
    }

    @Test
    public void givenNewAccountDto_whenCreateNewAccount_thenReturnNewAccount() {
        // given
        final NewAccountDto dto = NewAccountDto.builder()
                .accountNumber("12345678")
                .password("password")
                .username("username")
                .build();
        final AccountDto accountDto = new AccountDto("iban", "ownerId");
        final User user = new User(accountDto.getOwnerId(), dto.getUsername(), dto.getPassword());
        final Account account = new Account(accountDto.getIban(), user, dto.getAccountNumber());
        final String apiUrl = getApiUrl(dto.getAccountNumber());

        // when
        when(restTemplate.getForEntity(apiUrl, AccountDto.class))
                .thenReturn(ResponseEntity.of(Optional.of(accountDto)));
        when(passwordEncoder.encode(dto.getPassword())).thenReturn(dto.getPassword());
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        final NewAccountResponseDto newAccount = service.create(dto);

        assertThat(newAccount).isNotNull();
        assertThat(newAccount.getUsername()).isEqualTo(dto.getUsername());
        assertThat(newAccount.getAccountNumber()).isEqualTo(dto.getAccountNumber());
        assertThat(newAccount.getIban()).isEqualTo(accountDto.getIban());

        verify(restTemplate).getForEntity(apiUrl, AccountDto.class);
        verify(passwordEncoder).encode(dto.getPassword());
        verify(userRepository).save(any(User.class));
        verify(accountRepository).save(saveAccountArgumentCaptor.capture());

        final Account saveAccount = saveAccountArgumentCaptor.getValue();

        assertThat(saveAccount).isNotNull();
        assertThat(saveAccount.getAccountNumber()).isEqualTo(dto.getAccountNumber());
        assertThat(saveAccount.getIban()).isEqualTo(accountDto.getIban());
        assertThat(saveAccount.getUser()).isEqualTo(user);
    }

    private String getApiUrl(final String accountNumber) {
        return "https://localhost:8444/accounts/" + accountNumber;
    }

    @TestConfiguration
    static class AccountServiceTestContextConfiguration {
        @Bean
        public AccountService service() {
            return new AccountServiceImpl();
        }
    }
}