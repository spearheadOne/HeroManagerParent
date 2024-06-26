package org.abondar.industrial.authservice.service

import jakarta.inject.Singleton
import org.abondar.industrial.authservice.exception.UserNotFoundException
import org.abondar.industrial.authservice.exception.WrongPasswordException
import org.abondar.industrial.authservice.model.User
import org.abondar.industrial.authservice.model.UserResponse
import org.abondar.industrial.authservice.repo.UserRepository

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

    fun deleteUser(username: String): UserResponse {
        findUserHelper(username)

        userRepository.deleteByName(username)
        return UserResponse("User deleted successfully")
    }

    fun findUser(username: String): UserResponse {
        val user = findUserHelper(username)

        return UserResponse(user.name)
    }

   fun loginUser(user: User): UserResponse {
       val found = findUserHelper(user.name)
       if (found.passwordHash != user.passwordHash) {
           throw WrongPasswordException()
       }

       return UserResponse("User logged in successfully")
   }


  private fun findUserHelper(username: String): User {
      val user = userRepository.findByName(username)
      if (user.isEmpty) {
          throw UserNotFoundException()
      }
      return user.get();
  }
}