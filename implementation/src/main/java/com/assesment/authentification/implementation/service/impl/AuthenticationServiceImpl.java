package com.assesment.authentification.implementation.service.impl;

import com.assesment.authentification.implementation.repository.UserRepository;
import com.assesment.authentification.implementation.exception.AuthenticationFailedException;
import com.assesment.authentification.implementation.security.model.JwtUserDetails;
import com.assesment.authentification.implementation.security.service.AuthenticationHelper;
import com.assesment.authentification.implementation.service.AuthenticationService;
import com.assesment.authentification.implementation.service.UserService;
import com.assesment.authentification.implementation.service.dto.LoginRequestDto;
import com.assesment.authentification.implementation.service.dto.LoginResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationHelper authenticationHelper;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public LoginResponseDto login(final LoginRequestDto dto) {
        final String username = Optional.ofNullable(dto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("Username should be passed."));

        final String password = Optional.ofNullable(dto.getPassword())
                .orElseThrow(() -> new BadCredentialsException("Password should be passed."));

        final UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username,
                password);
        final Authentication authResult;

        // Try to authenticate with this token
        try {
            authResult = this.authenticationManager.authenticate(authRequest);
        } catch (final BadCredentialsException ex) {
            userService.onAuthenticationFailure(username);
            throw new AuthenticationFailedException("Username or password was incorrect. Please try again.", ex);
        }

        // Set generated JWT token to response header
        if (!authResult.isAuthenticated()) {
            throw new AuthenticationFailedException();
        }

        final JwtUserDetails userDetails = (JwtUserDetails) authResult.getPrincipal();

        final String token = this.authenticationHelper.generateToken(userDetails.getId());

        userRepository.clearFailureLoginCount(username);
        return new LoginResponseDto(token);
    }
}
