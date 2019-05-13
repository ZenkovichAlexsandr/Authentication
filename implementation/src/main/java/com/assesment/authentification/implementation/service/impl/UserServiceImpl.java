package com.assesment.authentification.implementation.service.impl;

import com.assesment.authentification.implementation.model.User;
import com.assesment.authentification.implementation.repository.UserRepository;
import com.assesment.authentification.implementation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {
    @Value("${authentication.maxAttemptCount}")
    private int maxAttemptCount;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onAuthenticationFailure(final String username) {
        final User user = userRepository.findByUsername(username);

        if (user == null) {
            return;
        }

        if (user.getFailureLoginCount() + 1 >= maxAttemptCount) {
            user.setBlockedTime(LocalDateTime.now());
            user.setFailureLoginCount(0);
        } else {
            user.setFailureLoginCount(user.getFailureLoginCount() + 1);
        }

        userRepository.save(user);
    }
}
