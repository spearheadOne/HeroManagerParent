package org.abondar.industrial.authservice.service

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.abondar.industrial.authservice.service.PasswordService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt

@MicronautTest
class PasswordServiceTest {

    @Inject
    lateinit var passwordService: PasswordService

    @Test
    fun `test hash passsword`() {
        val password = "testPwd"

        val hash = passwordService.hashPassword(password)
        assertNotNull(hash)

        val passwordIsWrong = passwordService.checkPassword("test",hash)
        assertFalse(passwordIsWrong)

        val passwordIsRight = passwordService.checkPassword("test",hash)
        assertFalse(passwordIsWrong)
    }

    @Test
    fun `test hash passsword wrong password`() {
        val password = "testPwd"

        val hash = passwordService.hashPassword(password)
        assertNotNull(hash)

        val passwordIsWrong = passwordService.checkPassword("test",hash)
        assertFalse(passwordIsWrong)
    }

    @Test
    fun `test hash passsword right password`() {
        val password = "testPwd"

        val hash = passwordService.hashPassword(password)
        assertNotNull(hash)

        val passwordIsRight = passwordService.checkPassword(password, hash)
        assertTrue(passwordIsRight)
    }
}