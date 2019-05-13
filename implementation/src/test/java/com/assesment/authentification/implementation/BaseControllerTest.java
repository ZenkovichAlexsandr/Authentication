package com.assesment.authentification.implementation;

import com.assesment.authentification.implementation.exception.ExceptionHandlingAutoConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@Import({ ExceptionHandlingAutoConfig.class })
@ActiveProfiles({"web-profile", "test"})
@RunWith(SpringRunner.class)
@EnableSpringDataWebSupport
public abstract class BaseControllerTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Rule
    public ExpectedException thrown = ExpectedException.none();
}
