package org.abondar.industrial.authservice.service

import jakarta.inject.Singleton
import org.mindrot.jbcrypt.BCrypt

@Singleton
class PasswordService {

    fun hashPassword(password: String): String {
        val saltRounds = 10
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(saltRounds))

        return hashedPassword
    }

    fun checkPassword(password: String, hash: String): Boolean {
        return BCrypt.checkpw(password, hash)
    }
}