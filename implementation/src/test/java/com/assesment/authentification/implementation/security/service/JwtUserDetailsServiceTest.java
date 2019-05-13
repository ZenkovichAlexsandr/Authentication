package com.assesment.authentification.implementation.security.service;

import com.assesment.authentification.implementation.BaseServiceTest;
import com.assesment.authentification.implementation.model.User;
import com.assesment.authentification.implementation.repository.UserRepository;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class JwtUserDetailsServiceTest extends BaseServiceTest {

    @Autowired
    private UserDetailsService userDetailsService;

    @MockBean
    private UserRepository userRepository;

    @After
    public void after() {
        verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void givenUsername_whenLoadUser_thenReturnUserDetails() throws Exception {
        // given
        final String username = "user";
        final String password = "password";
        final User user = new User("id", username, password);

        // when
        when(this.userRepository.findByUsername(username)).thenReturn(user);

        // execute
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

        // then
        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(UserDetails.class)
                .hasFieldOrPropertyWithValue("id", user.getId())
                .hasFieldOrPropertyWithValue("username", user.getUsername())
                .hasFieldOrPropertyWithValue("password", user.getPassword());

        verify(this.userRepository).findByUsername(username);
    }

    @Test
    public void givenUsernameWithNotExistedUser_whenLoadUser_thenReturnException() throws Exception {
        // given
        final String username = "user";

        // when
        when(this.userRepository.findByUsername(username)).thenReturn(null);

        // expect
        this.thrown.expect(UsernameNotFoundException.class);
        this.thrown.expectMessage("User nor found.");

        // execute & then
        try {
            this.userDetailsService.loadUserByUsername(username);
        } finally {
            verify(this.userRepository).findByUsername(username);
        }
    }

    @TestConfiguration
    static class JwtUserDetailsServiceTestContextConfiguration {

        @Bean
        public UserDetailsService userDetailsService() {
            return new JwtUserDetailsServiceImpl();
        }
    }

}
