package org.abondar.industrial.authservice.auth

import io.micronaut.security.authentication.AuthenticationException
import org.abondar.industrial.authservice.model.User
import java.util.*

class DecodeUtil {

    companion object {

        const val CREDENTIALS_HEADER = "Credentials"

        @JvmStatic
        fun decodeCredentials(credHeader: String): User {
            if (credHeader.isEmpty()){
                throw AuthenticationException("Credentials are empty")
            }

            val credentialsString = String(Base64.getDecoder().decode(credHeader))
            val parts = credentialsString.split(":", limit = 2)

            return User(null,parts[0],parts[1])
        }
    }
}