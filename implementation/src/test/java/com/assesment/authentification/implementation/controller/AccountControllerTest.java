package com.assesment.authentification.implementation.controller;

import com.assesment.authentification.implementation.BaseControllerTest;
import com.assesment.authentification.implementation.service.AccountService;
import com.assesment.authentification.implementation.service.dto.NewAccountDto;
import com.assesment.authentification.implementation.service.dto.NewAccountResponseDto;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.is;

@WebMvcTest(value = AccountController.class, secure = false)
public class AccountControllerTest extends BaseControllerTest {
    @MockBean
    private AccountService service;

    @Test
    public void givenNewAccount_whenCreate_thenReturnNewAccount() throws Exception {
        // given
        final NewAccountResponseDto account = getNewAccountResponseDto();

        // when
        when(service.create(any(NewAccountDto.class))).thenReturn(account);

        // then
        mvc.perform(post("/account")
                .content(objectMapper.writeValueAsString(getNewAccountDto()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username", is(account.getUsername())))
                .andExpect(jsonPath("$.accountNumber", is(account.getAccountNumber())))
                .andExpect(jsonPath("$.iban", is(account.getIban())));
    }

    @Test
    public void givenNewAccountWithNullUsername_whenCreate_thenThrowException() throws Exception {
        // given
        final NewAccountDto newAccount = getNewAccountDto();
        newAccount.setUsername(null);

        // then
        mvc.perform(post("/account")
                .content(objectMapper.writeValueAsString(newAccount))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",
                        containsString("Username can not be empty")));
    }

    @Test
    public void givenNewAccountWithBlankUsername_whenCreate_thenThrowException() throws Exception {
        // given
        final NewAccountDto newAccount = getNewAccountDto();
        newAccount.setUsername("   ");

        // then
        mvc.perform(post("/account")
                .content(objectMapper.writeValueAsString(newAccount))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",
                        containsString("Username can not be blank")));
    }

    @Test
    public void givenNewAccountWithBlankPassword_whenCreate_thenThrowException() throws Exception {
        // given
        final NewAccountDto newAccount = getNewAccountDto();
        newAccount.setPassword("      ");

        // then
        mvc.perform(post("/account")
                .content(objectMapper.writeValueAsString(newAccount))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",
                        containsString("Password can not be blank")));
    }

    @Test
    public void givenNewAccountWithIncorrectPasswordLength_whenCreate_thenThrowException() throws Exception {
        // given
        final NewAccountDto newAccount = getNewAccountDto();
        newAccount.setPassword("1234");

        // then
        mvc.perform(post("/account")
                .content(objectMapper.writeValueAsString(newAccount))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",
                        containsString("Password should be more than 6 symbols")));
    }

    @Test
    public void givenNewAccountWithIncorrectAccountNumberLength_whenCreate_thenThrowException() throws Exception {
        // given
        final NewAccountDto newAccount = getNewAccountDto();
        newAccount.setAccountNumber("1234");

        // then
        mvc.perform(post("/account")
                .content(objectMapper.writeValueAsString(newAccount))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("$.message",
                        containsString("Account Number should contains 8 numbers")));
    }

    private NewAccountDto getNewAccountDto() {
        return  NewAccountDto.builder()
                .username("username")
                .password("usernamepassword")
                .accountNumber("12345678")
                .build();
    }

    private NewAccountResponseDto getNewAccountResponseDto() {
        return NewAccountResponseDto.builder()
                .username("username")
                .iban("iban")
                .accountNumber("12345678")
                .build();
    }
}
