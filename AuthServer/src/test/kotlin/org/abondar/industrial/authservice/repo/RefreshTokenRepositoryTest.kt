package org.abondar.industrial.authservice.repo

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.abondar.industrial.authservice.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant

@MicronautTest
class RefreshTokenRepositoryTest {

    @Inject
    lateinit var refreshTokenRepository: RefreshTokenRepository

    @Inject
    lateinit var userRepository: UserRepository


    @Test
    fun `test find token`() {
        val user = User(name = "John Doe", password = "hash")
        userRepository.save(user)


        val token = refreshTokenRepository.save(user,"test",false, Instant.now())
        assertNotNull(token.id)

        val res = refreshTokenRepository.findByToken(token.token)
        assertFalse(res.isEmpty)
        assertEquals(token.token, res.get().token)
        assertEquals(user.id, res.get().user?.id)
    }


    @Test
    fun `test update token`() {
        val user = User(name = "John Doe", password = "hash")
        userRepository.save(user)


        val token = refreshTokenRepository.save(user,"test",false, Instant.now())
        assertNotNull(token.id)

        val res = refreshTokenRepository.updateRevokedByToken(token.token,true)
        assertEquals(1,res)
    }
}