package com.assesment.authentification.implementation.repository;

import com.assesment.authentification.implementation.BaseRepositoryTest;
import com.assesment.authentification.implementation.model.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest extends BaseRepositoryTest {
    @Autowired
    private UserRepository repository;

    @Test
    public void givenUserWithNotUniqueUsername_whenSave_thenThrowException() {
        // given
        final User user = new User("1", "username", "password");

        testEntityManager.persist(user);
        testEntityManager.flush();

        // expect
        this.thrown.expect(PersistenceException.class);

        // when
        final User notUniqueUser = new User("3", "username", "password");
        testEntityManager.persist(notUniqueUser);
        testEntityManager.flush();
    }

    @Test
    public void givenUsername_whenFindByUsername_thenReturnUser() {
        // given
        final User user = new User("1", "username", "password");

        testEntityManager.persist(user);
        testEntityManager.flush();

        final User find = repository.findByUsername(user.getUsername());

        assertThat(find).isNotNull();
    }
}
