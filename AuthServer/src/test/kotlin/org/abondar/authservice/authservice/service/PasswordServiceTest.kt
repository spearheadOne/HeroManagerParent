package org.abondar.authservice.authservice.service

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.abondar.authservice.service.PasswordService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt

@MicronautTest
class PasswordServiceTest {

    @Inject
    lateinit var passwordService: PasswordService

    @Test
    fun `test create user`() {
        val password = "testPwd"

        val hash = passwordService.hashPassword(password)
        assertNotNull(hash)

        val passwordIsWrong = BCrypt.checkpw("test", hash)
        assertFalse(passwordIsWrong)
    }
}