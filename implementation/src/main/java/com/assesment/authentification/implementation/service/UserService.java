package com.assesment.authentification.implementation.service;

public interface UserService {
    /**
     * On failure authentication.
     * Increase login failure count every time when user type incorrect password.
     * If login failure count >= 3 we set current time to blocked time.
     * It means that user will not be able to login 24 hours.
     *
     * @param username - username of an user
     */
    void onAuthenticationFailure(String username);
}
