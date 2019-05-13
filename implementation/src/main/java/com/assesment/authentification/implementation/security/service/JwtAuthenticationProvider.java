package com.assesment.authentification.implementation.security.service;

import com.assesment.authentification.implementation.model.User;
import com.assesment.authentification.implementation.repository.UserRepository;
import com.assesment.authentification.implementation.security.exception.ExpiredTokenAuthenticationException;
import com.assesment.authentification.implementation.security.exception.InvalidTokenAuthenticationException;
import com.assesment.authentification.implementation.security.model.JwtAuthenticationToken;
import com.assesment.authentification.implementation.security.model.JwtUserDetails;
import com.assesment.authentification.implementation.security.model.TokenPayload;
import liquibase.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private static final long MILLIS_IN_SECOND = 1000L;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationHelper authenticationHelper;

    @Override
    public Authentication authenticate(final Authentication authRequest) {
        // Getting string token from authentication request object
        final String token = StringUtils.trimToNull((String) authRequest.getCredentials());

        //  Deserialize token
        final TokenPayload tokenPayload = authenticationHelper.decodeToken(token);

        // Checking if token already expired and throwing an AuthenticationException in this case
        checkIsExpired(tokenPayload.getExp());

        // Getting user id from token
        final String userEntityId = tokenPayload.getUserId();
        if (Objects.isNull(userEntityId)) {
            throw new InvalidTokenAuthenticationException("Token does not contain a user id.");
        }

        // Getting user from database
        final User user = userRepository.findById(userEntityId)
                .orElseThrow(() ->  new InvalidTokenAuthenticationException("Token does not contain existed user id."));

        // Return authenticated Authentication
        final JwtUserDetails userDetails = new JwtUserDetails(user);
        return new JwtAuthenticationToken(userDetails);
    }

    private void checkIsExpired(final Long tokenExpirationTime) {
        if ((System.currentTimeMillis() / MILLIS_IN_SECOND) > tokenExpirationTime) {
            throw new ExpiredTokenAuthenticationException();
        }
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
