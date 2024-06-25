package org.abondar.industrial.authservice.model

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.abondar.industrial.authservice.model.User
import org.abondar.industrial.authservice.repo.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class UserRepositoryTest {

    @Inject
    lateinit var userRepository: UserRepository


    @Test
    fun `test find user`() {
        val user = User(name = "John Doe", passwordHash = "hash")
        userRepository.save(user)

        assertNotNull(user.id)

        var res = userRepository.findByName(user.name)
        assertFalse(res.isEmpty)
        assertEquals(user.name, res.get().name)

    }


}