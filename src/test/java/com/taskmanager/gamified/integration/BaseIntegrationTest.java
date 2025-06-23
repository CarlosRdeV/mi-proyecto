package com.taskmanager.gamified.integration;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class BaseIntegrationTest {
    // Clase base para todas las pruebas de integración de repositorios
}