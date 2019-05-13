package com.assesment.authentification.implementation;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles({ "service-test", "test" })
@RunWith(SpringRunner.class)
@JsonTest
public abstract class BaseServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
}
