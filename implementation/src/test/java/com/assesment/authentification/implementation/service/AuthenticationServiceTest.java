package com.assesment.authentification.implementation.service;

import com.assesment.authentification.implementation.BaseServiceTest;
import com.assesment.authentification.implementation.exception.AuthenticationFailedException;
import com.assesment.authentification.implementation.model.User;
import com.assesment.authentification.implementation.repository.UserRepository;
import com.assesment.authentification.implementation.security.model.JwtAuthenticationToken;
import com.assesment.authentification.implementation.security.model.JwtUserDetails;
import com.assesment.authentification.implementation.security.service.AuthenticationHelper;
import com.assesment.authentification.implementation.service.dto.LoginRequestDto;
import com.assesment.authentification.implementation.service.dto.LoginResponseDto;
import com.assesment.authentification.implementation.service.impl.AuthenticationServiceImpl;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest extends BaseServiceTest {
    @Autowired
    private AuthenticationService service;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private UserService userService;
    @MockBean
    private AuthenticationHelper authenticationHelper;
    @MockBean
    private AuthenticationManager authenticationManager;

    @After
    public void after() {
        verifyNoMoreInteractions(
                userService,
                userRepository,
                authenticationHelper,
                authenticationManager
        );
    }

    @Test
    public void givenLoginRequestDto_whenLogin_thenReturnLogin() {
        // given
        final LoginRequestDto request = new LoginRequestDto("username", "password");
        final JwtUserDetails userDetails = new JwtUserDetails(new User("id", "username", "password"));
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(userDetails);

        // when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authenticationHelper.generateToken(userDetails.getId())).thenReturn("token");
        doNothing().when(userRepository).clearFailureLoginCount(request.getUsername());

        final LoginResponseDto response = service.login(request);

        // then
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authenticationHelper).generateToken(userDetails.getId());
        verify(userRepository).clearFailureLoginCount(request.getUsername());

        assertThat(response).isNotNull();
    }

    @Test
    public void givenEmptyUsernameLoginRequestDto_whenLogin_thenThrowException() {
        // given
        final LoginRequestDto request = new LoginRequestDto();

        // expect
        this.thrown.expect(BadCredentialsException.class);
        this.thrown.expectMessage("Username should be passed.");

        //then
        service.login(request);
    }

    @Test
    public void givenEmptyPasswordLoginRequestDto_whenLogin_thenThrowException() {
        // given
        final LoginRequestDto request = new LoginRequestDto("ss", null);

        // expect
        this.thrown.expect(BadCredentialsException.class);
        this.thrown.expectMessage("Password should be passed.");

        //then
        service.login(request);
    }

    @Test
    public void givenNotAuthenticatedLoginRequestDto_whenLogin_thenThrowException() {
        // given
        final LoginRequestDto request = new LoginRequestDto("username", "password");
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken("");

        // when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // expect
        this.thrown.expect(AuthenticationFailedException.class);

        // execute & then
        try {
            service.login(request);
        } finally {
            verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }
    }

    @TestConfiguration
    static class AuthenticationServiceTestContextConfiguration {
        @Bean
        public AuthenticationService service() {
            return new AuthenticationServiceImpl();
        }
    }
}
