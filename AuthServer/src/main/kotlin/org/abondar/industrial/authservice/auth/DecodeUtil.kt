package org.abondar.industrial.authservice.auth

import io.micronaut.security.authentication.AuthenticationException
import org.abondar.industrial.authservice.model.User
import java.util.*

class DecodeUtil {

    companion object {

        @JvmStatic
        fun decodeCredentials(authHeader: String): User {
            if (authHeader.isEmpty()){
                throw AuthenticationException("Credentials are empty")
            }
            val base64Credentials = authHeader.removePrefix("Basic: ")
            val credentialsString = String(Base64.getDecoder().decode(base64Credentials))
            val parts = credentialsString.split(":", limit = 2)

            return User(null,parts[0],parts[1])
        }
    }
}