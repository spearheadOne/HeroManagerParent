package org.abondar.industrial.authservice.auth

import io.micronaut.http.HttpRequest
import io.micronaut.security.authentication.*
import io.micronaut.security.authentication.provider.HttpRequestAuthenticationProvider
import jakarta.inject.Inject
import jakarta.inject.Singleton
import org.abondar.industrial.authservice.auth.DecodeUtil.Companion.decodeCredentials
import org.abondar.industrial.authservice.repo.UserRepository
import org.abondar.industrial.authservice.service.PasswordService

@Singleton
class UserAuthneticationProvider<B> @Inject constructor(
    private val userRepository: UserRepository,
    private val passwordService: PasswordService,
) : HttpRequestAuthenticationProvider<B> {


    override fun authenticate(
        requestContext: HttpRequest<B>?,
        authRequest: AuthenticationRequest<String, String>?
    ): AuthenticationResponse {
        val identity = authRequest?.identity ?: ""
        val secret = authRequest?.secret ?: ""

        val user = userRepository.findByName(identity)
        return if (user.isPresent && passwordService.checkPassword(secret, user.get().password)) {
            AuthenticationResponse.success(identity)
        } else {
            AuthenticationResponse.failure(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH)
        }

    }
}