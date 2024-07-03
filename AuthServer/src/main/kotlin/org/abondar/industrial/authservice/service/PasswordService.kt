package org.abondar.industrial.authservice.service

import jakarta.inject.Singleton
import org.mindrot.jbcrypt.BCrypt
import org.slf4j.LoggerFactory

@Singleton
class PasswordService {

    private val log = LoggerFactory.getLogger(PasswordService::class.java)

    fun hashPassword(password: String): String {
        val saltRounds = 10
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt(saltRounds))

        log.info("Password hashed")
        return hashedPassword
    }

    fun checkPassword(password: String, hash: String): Boolean {

        log.info("Verifying password")
        return BCrypt.checkpw(password, hash)
    }
}