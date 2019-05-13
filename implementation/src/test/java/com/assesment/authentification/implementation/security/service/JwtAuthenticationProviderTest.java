package com.assesment.authentification.implementation.security.service;

import com.assesment.authentification.implementation.BaseServiceTest;
import com.assesment.authentification.implementation.model.User;
import com.assesment.authentification.implementation.repository.UserRepository;
import com.assesment.authentification.implementation.security.exception.ExpiredTokenAuthenticationException;
import com.assesment.authentification.implementation.security.exception.InvalidTokenAuthenticationException;
import com.assesment.authentification.implementation.security.model.JwtAuthenticationToken;
import com.assesment.authentification.implementation.security.model.TokenPayload;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class JwtAuthenticationProviderTest extends BaseServiceTest {
    @Autowired
    private JwtAuthenticationProvider provider;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private AuthenticationHelper authenticationHelper;

    @After
    public void after() {
        verifyNoMoreInteractions(this.userRepository, this.authenticationHelper);
    }

    @Test
    public void givenAuthentication_whenAuthenticate_thenReturnAuthenticate() {
        // given
        final String token = "token";
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(token);
        final TokenPayload tokenPayload = new TokenPayload("userId", 999999999999999999L);
        final User user = new User("userId", "username", "password");

        // when
        when(authenticationHelper.decodeToken(token)).thenReturn(tokenPayload);
        when(userRepository.findById(tokenPayload.getUserId())).thenReturn(Optional.of(user));

        final Authentication newAuthentication = provider.authenticate(authentication);

        // then
        verify(authenticationHelper).decodeToken(token);
        verify(userRepository).findById(tokenPayload.getUserId());

        assertThat(newAuthentication).isNotNull();
    }

    @Test
    public void givenExpiredAuthentication_whenAuthenticate_thenThrowException() {
        // given
        final String token = "token";
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(token);
        final TokenPayload tokenPayload = new TokenPayload("userId", 1);

        // when
        when(authenticationHelper.decodeToken(token)).thenReturn(tokenPayload);

        // expect
        this.thrown.expect(ExpiredTokenAuthenticationException.class);

        // execute & then
        try {
            provider.authenticate(authentication);
        } finally {
            verify(authenticationHelper).decodeToken(token);
        }
    }

    @Test
    public void givenAuthenticationWithIncorrectUser_whenAuthenticate_thenThrowException() {
        // given
        final String token = "token";
        final JwtAuthenticationToken authentication = new JwtAuthenticationToken(token);
        final TokenPayload tokenPayload = new TokenPayload("userId", 999999999999999999L);

        // when
        when(authenticationHelper.decodeToken(token)).thenReturn(tokenPayload);
        when(userRepository.findById(tokenPayload.getUserId())).thenReturn(Optional.empty());

        // expect
        this.thrown.expect(InvalidTokenAuthenticationException.class);
        this.thrown.expectMessage("Token does not contain existed user id.");

        // execute & then
        try {
            provider.authenticate(authentication);
        } finally {
            verify(authenticationHelper).decodeToken(token);
            verify(userRepository).findById(tokenPayload.getUserId());
        }
    }

    @TestConfiguration
    static class JwtAuthenticationProviderTestContextConfiguration {
        @Bean
        public JwtAuthenticationProvider provider() {
            return new JwtAuthenticationProvider();
        }
    }
}
