package org.abondar.authservice.authservice.service

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.abondar.authservice.exception.UserNotFoundException
import org.abondar.authservice.model.User
import org.abondar.authservice.service.UserService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@MicronautTest
class UserServiceTest {

    @Inject
    lateinit var userService: UserService


    @Test
    fun `test create user`() {
        val user = User(name = "John Doe", passwordHash = "hash")

        val resp = userService.createUser(user)

        assertNotNull(resp)
    }


    @Test
    fun `test update user`() {
        val user = User(name = "John Doe", passwordHash = "hash")
        userService.createUser(user)

        val upd = user.copy(passwordHash = "hash")
        val resp = userService.updateUser(upd)

        assertNotNull(resp)
        assertEquals("User updated successfully",resp.result)
    }

    @Test
    fun `test update user not created before`() {
        val user = User(name = "John Doe", passwordHash = "hash")

        val resp = userService.updateUser(user)

        assertNotNull(resp)
        assertEquals("User updated successfully",resp.result)
    }


    @Test
    fun `test find user`() {
        val user = User(name = "John Doe", passwordHash = "hash")
        userService.createUser(user)

        val resp = userService.findUser(user.name)

        assertNotNull(resp)
        assertEquals(user.name,resp.result, )
    }

    @Test
    fun `test find user not found`() {

        assertThrows<UserNotFoundException> {
            userService.findUser("test")
        }

    }


    @Test
    fun `test delete user`() {
        val user = User(name = "John Doe", passwordHash = "hash")
        userService.createUser(user)

        val resp = userService.deleteUser(user)

        assertNotNull(resp)
        assertEquals("User deleted successfully", resp.result)
    }

}