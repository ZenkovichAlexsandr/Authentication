package com.assesment.authentification.implementation.service;

import com.assesment.authentification.implementation.BaseServiceTest;
import com.assesment.authentification.implementation.model.User;
import com.assesment.authentification.implementation.model.UserRole;
import com.assesment.authentification.implementation.repository.UserRepository;
import com.assesment.authentification.implementation.service.impl.UserServiceImpl;
import org.junit.After;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

public class UserServiceTest extends BaseServiceTest {
    @Autowired
    private UserService service;
    @MockBean
    private UserRepository userRepository;
    @Captor
    private ArgumentCaptor<User> saveUserArgumentCaptor;

    @After
    public void after() {
        verifyNoMoreInteractions(
                userRepository
        );
    }

    @Test
    public void givenNotExistsUsername_whenOnAuthenticationFailure_thenDoNothing() {
        // given
        final String username = "username";

        // when
        when(userRepository.findByUsername(username)).thenReturn(null);

        service.onAuthenticationFailure(username);

        // then
        verify(userRepository).findByUsername(username);
    }

    @Test
    public void givenUsername_whenOnAuthenticationFailure_thenIncreaseFailureLoginCount() {
        // given
        final User user = new User("id", "username", "password");

        // when
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        service.onAuthenticationFailure(user.getUsername());

        // then
        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).save(saveUserArgumentCaptor.capture());

        final User newUser = saveUserArgumentCaptor.getValue();

        assertThat(newUser).isNotNull();
        assertThat(newUser.getFailureLoginCount()).isEqualTo(1);
    }

    @Test
    public void givenUsername_whenOnAuthenticationFailure_thenSetBlockedTime() {
        // given
        final User user = new User("id", "username", "password", UserRole.ROLE_USER, 2, null);

        // when
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userRepository.save(any(User.class))).thenReturn(user);

        service.onAuthenticationFailure(user.getUsername());

        // then
        verify(userRepository).findByUsername(user.getUsername());
        verify(userRepository).save(saveUserArgumentCaptor.capture());

        final User newUser = saveUserArgumentCaptor.getValue();

        assertThat(newUser).isNotNull();
        assertThat(newUser.getFailureLoginCount()).isEqualTo(0);
        assertThat(newUser.getBlockedTime()).isNotNull();
    }

    @TestConfiguration
    static class UserServiceTestContextConfiguration {
        @Bean
        public UserService service() {
            return new UserServiceImpl();
        }
    }
}