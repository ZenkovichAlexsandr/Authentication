package com.assesment.authentification.implementation;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles({"repository-test", "test"})
@RunWith(SpringRunner.class)
@DataJpaTest
public abstract class BaseRepositoryTest {

    @Autowired
    protected TestEntityManager testEntityManager;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
}
