package com.assesment.authentification.implementation.repository;

import com.assesment.authentification.implementation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    @Modifying
    @Query("UPDATE User as u SET u.failureLoginCount = 0 WHERE u.username = :username")
    void clearFailureLoginCount(String username);
}
