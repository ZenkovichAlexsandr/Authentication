package com.assesment.authentification.implementation.security.service;

import com.assesment.authentification.implementation.security.exception.InvalidTokenAuthenticationException;
import com.assesment.authentification.implementation.security.model.TokenPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.MacSigner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Objects;

@Component
public class AuthenticationHelper {
    public static final String AUTHENTICATION_HEADER = "Authorization";

    @Value("${authentication.token.secret}")
    private String secret;
    @Value("${authentication.token.expiration}")
    private Long tokenExpiration;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * Generate token based on user id.
     *
     * @param userId - id of an user
     * @return generated token
     */
    public String generateToken(final String userId) {
        try {
            final TokenPayload payload = new TokenPayload(
                    userId,
                    Instant.now().getEpochSecond() + this.tokenExpiration
            );

            final String token = this.objectMapper.writeValueAsString(payload);
            return JwtHelper.encode(token, new MacSigner(secret)).getEncoded();
        } catch (JsonProcessingException exception) {
            throw new InternalAuthenticationServiceException("Error generating token.", exception);
        }
    }

    /**
     * Decode token and verify token.
     *
     * @param token - token, that should be decoded.
     * @return token payload {@link TokenPayload}
     */
    public TokenPayload decodeToken(final String token) {
        if (Objects.isNull(token)) {
            throw new InvalidTokenAuthenticationException("Token was null or blank.");
        }

        // Getting JWT object from string token
        final Jwt jwt = JwtHelper.decode(token);

        // Validate token signature (to be sure that token has not been tampered with)
        try {
            jwt.verifySignature(new MacSigner(secret));
        } catch (Exception exception) {
            throw new InvalidTokenAuthenticationException("Token signature verification failed.", exception);
        }

        // Getting payload of token
        final String claims = jwt.getClaims();

        try {
            return this.objectMapper.readValue(claims, TokenPayload.class);
        } catch (IOException exception) {
            throw new InvalidTokenAuthenticationException("Token parsing failed.", exception);
        }
    }
}
