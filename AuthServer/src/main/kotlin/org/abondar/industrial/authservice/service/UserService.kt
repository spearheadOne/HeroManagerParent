package org.abondar.industrial.authservice.service

import io.micronaut.security.authentication.AuthenticationException
import jakarta.inject.Singleton
import org.abondar.industrial.authservice.exception.UserNotFoundException
import org.abondar.industrial.authservice.model.User
import org.abondar.industrial.authservice.model.UserResponse
import org.abondar.industrial.authservice.repo.UserRepository

@Singleton
class UserService(private val userRepository: UserRepository,
                  private val passwordService: PasswordService) {

    fun createUser(user: User): UserResponse {
        user.password = passwordService.hashPassword(user.password)
        userRepository.save(user)
        return UserResponse(user.id.toString())
    }

    fun updateUser(user: User): UserResponse {
        val res = userRepository.findById(user.id)
        if (res.isEmpty) {
            throw UserNotFoundException()
        }

        val usr = res.get()
        usr.name= user.name
        usr.password = passwordService.hashPassword(user.password)

        userRepository.update(usr)
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




  private fun findUserHelper(username: String): User {
      val user = userRepository.findByName(username)
      if (user.isEmpty) {
          throw UserNotFoundException()
      }
      return user.get();
  }
}