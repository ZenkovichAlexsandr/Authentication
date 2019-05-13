package com.assesment.authentification.implementation.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private String id;
    private String username;
    private String password;

    public User(final String id, final String username, final String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = UserRole.ROLE_USER;
    }

    @Column
    @Enumerated(EnumType.STRING)
    private UserRole role;

    private int failureLoginCount;

    private LocalDateTime blockedTime;
}
