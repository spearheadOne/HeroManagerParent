package org.abondar.industrial.authservice.service

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.abondar.industrial.authservice.exception.UserNotFoundException
import org.abondar.industrial.authservice.model.User
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
        val user = User(name = "John Doe", password = "hash")

        val resp = userService.createUser(user)

        assertNotNull(resp)
    }


    @Test
    fun `test update user`() {
        val user = User(name = "John Doe", password = "hash")
        userService.createUser(user)

        val upd = user.copy(password = "hash")
        val resp = userService.updateUser(upd)

        assertNotNull(resp)
        assertEquals("User updated successfully",resp.result)
    }

    @Test
    fun `test update user not found`() {
        val user = User(id=100, name = "John Doe", password = "hash")

        assertThrows<UserNotFoundException> {
            userService.updateUser(user)
        }

    }


    @Test
    fun `test find user`() {
        val user = User(name = "John Doe", password = "hash")
        userService.createUser(user)

        val resp = userService.findUser(user.name)

        assertNotNull(resp)
        assertEquals(user.name,resp.result)
    }

    @Test
    fun `test find user not found`() {

        assertThrows<UserNotFoundException> {
            userService.findUser("xxx")
        }

    }


    @Test
    fun `test delete user`() {
        val user = User(name = "John Doe", password = "hash")
        userService.createUser(user)

        val resp = userService.deleteUser(user.name)

        assertNotNull(resp)
        assertEquals("User deleted successfully", resp.result)
    }


}