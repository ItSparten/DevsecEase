package com.cozy;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootTest
@PropertySource("classpath:application.properties")
@EnableJpaRepositories(basePackages = "com.cozy")
class CozyApplicationTests {

    @Test
    void contextLoads() {
    }

}
