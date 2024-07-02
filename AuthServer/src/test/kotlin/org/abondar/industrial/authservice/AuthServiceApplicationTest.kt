package org.abondar.industrial.authservice

import io.micronaut.runtime.EmbeddedApplication
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest
class AuthServiceApplicationTest {

    @Inject
    lateinit var application: EmbeddedApplication<*>

    @Test
    fun `test application starts`() {
        Assertions.assertTrue(application.isRunning)
    }
}