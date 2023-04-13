package com.vattenfall.bookstore.utils

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import com.vattenfall.bookstore.infrastructure.persistence.BookDao
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.testcontainers.junit.jupiter.Testcontainers
import spock.lang.Specification

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
@Testcontainers
@ActiveProfiles("integration")
class BaseIntegration extends Specification {

    Faker faker = new Faker(Locale.UK)

    @Autowired
    protected BookDao bookGeneratedRepo

    @Autowired
    protected ObjectMapper objectMapper

    @Autowired
    protected MockMvc mvc

}
