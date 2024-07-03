package org.abondar.industrial.authservice.repo

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.abondar.industrial.authservice.model.User
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class UserRepositoryTest {

    @Inject
    lateinit var userRepository: UserRepository


    @Test
    fun `test find user`() {
        val user = User(name = "John Doe", password = "hash")
        userRepository.save(user)

        assertNotNull(user.id)

        var res = userRepository.findByName(user.name)
        assertFalse(res.isEmpty)
        assertEquals(user.name, res.get().name)

    }

    @Test
    fun `test delete user`() {
        val user = User(name = "John Doe", password = "hash")
        userRepository.save(user)

        userRepository.deleteByName(user.name)

        val res = userRepository.findByName(user.name)
        assertTrue(res.isEmpty())
    }


}