package com.assesment.authentification.implementation.controller;

import com.assesment.authentification.implementation.BaseControllerTest;
import com.assesment.authentification.implementation.service.AuthenticationService;
import com.assesment.authentification.implementation.service.dto.LoginRequestDto;
import com.assesment.authentification.implementation.service.dto.LoginResponseDto;
import org.junit.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AuthenticationController.class, secure = false)
public class AuthenticationControllerTest extends BaseControllerTest {
    @MockBean
    private AuthenticationService service;

    @Test
    public void givenLoginDto_whenLogin_thenReturnToken() throws Exception {
        // given
        final LoginResponseDto token = new LoginResponseDto("token");

        // when
        when(service.login(any(LoginRequestDto.class))).thenReturn(token);

        // then
        mvc.perform(post("/auth/login")
                .content(objectMapper.writeValueAsString(new LoginRequestDto("username", "password")))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is(token.getToken())));
    }
}
