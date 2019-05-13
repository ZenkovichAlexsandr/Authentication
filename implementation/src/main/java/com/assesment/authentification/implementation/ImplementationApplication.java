package com.assesment.authentification.implementation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({SecurityConfiguration.class, WebConfiguration.class})
public class ImplementationApplication {
    public static void main(final String[] args) {
        SpringApplication.run(ImplementationApplication.class, args);
    }
}
