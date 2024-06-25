package org.abondar.authservice.service

import jakarta.inject.Singleton
import org.abondar.authservice.exception.UserNotFoundException
import org.abondar.authservice.model.User
import org.abondar.authservice.model.UserResponse
import org.abondar.authservice.repo.UserRepository

@Singleton
class UserService(private val userRepository: UserRepository) {

    fun createUser(user: User): UserResponse {
        userRepository.save(user)
        return UserResponse("User created successfully with id: ${user.id}")
    }

    fun updateUser(user: User): UserResponse {
        userRepository.update(user)
        return UserResponse("User updated successfully")
    }

    fun deleteUser(user: User): UserResponse {
        findUser(user.name)

        userRepository.delete(user)
        return UserResponse("User deleted successfully")
    }

    fun findUser(username: String): UserResponse {
        val user = userRepository.findByName(username)
        if (user.isEmpty) {
            throw UserNotFoundException()
        }

        return UserResponse(user.get().name)
    }


}