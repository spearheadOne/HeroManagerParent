package org.abondar.industrial.authservice.repo

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant

@MicronautTest
class RefreshTokenRepositoryTest {

    @Inject
    lateinit var refreshTokenRepository: RefreshTokenRepository


    @Test
    fun `test find token`() {
        val token = refreshTokenRepository.save("test","test",false, Instant.now())
        assertNotNull(token.id)

        val res = refreshTokenRepository.findByToken(token.token)
        assertFalse(res.isEmpty)
        assertEquals(token.token, res.get().token)
    }


    @Test
    fun `test update token`() {
        val token = refreshTokenRepository.save("test","test",false, Instant.now())
        assertNotNull(token.id)

        val res = refreshTokenRepository.updateRevokedByUsername(token.username,true)
        assertEquals(1,res)
    }
}