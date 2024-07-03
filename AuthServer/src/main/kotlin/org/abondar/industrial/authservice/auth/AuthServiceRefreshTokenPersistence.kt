package org.abondar.industrial.authservice.auth

import io.micronaut.security.authentication.Authentication
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import jakarta.inject.Singleton
import org.abondar.industrial.authservice.model.RefreshToken
import org.abondar.industrial.authservice.repo.RefreshTokenRepository
import org.abondar.industrial.authservice.repo.UserRepository
import org.reactivestreams.Publisher
import org.slf4j.LoggerFactory
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*

@Singleton
class AuthServiceRefreshTokenPersistence(
    private val userRepository: UserRepository,
    private val refreshTokenRepository: RefreshTokenRepository
) : RefreshTokenPersistence {

    private val log = LoggerFactory.getLogger(AuthServiceRefreshTokenPersistence::class.java)

    override fun persistToken(event: RefreshTokenGeneratedEvent?) {
        if (event?.refreshToken != null && event.authentication?.name != null) {
            log.info("Saving refresh token")
            val user = userRepository.findByName(event.authentication.name)
            if (user.isEmpty){
               throw OauthErrorResponseException(
                    IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                    "User not found",
                    null
                )
            }
            refreshTokenRepository.save(user.get(),event.refreshToken,false, Instant.now())
        }
    }

    override fun getAuthentication(refreshToken: String?): Publisher<Authentication> {
        return Flux.create { emitter ->

            val nonNullableRefreshToken = refreshToken ?: run {
                log.info("Refresh token is null")
                emitter.error(
                    OauthErrorResponseException(
                        IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                        "Refresh token is null",
                        null
                    )
                )
                return@create
            }

            val tokenOpt: Optional<RefreshToken> = refreshTokenRepository.findByToken(nonNullableRefreshToken)
            if (tokenOpt.isPresent) {
                val token = tokenOpt.get()
                if (token.isRevoked) {
                    log.info("Refresh token revoked")
                    emitter.error(
                        OauthErrorResponseException(
                            IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                            "Refresh token revoked",
                            null
                        )
                    )
                } else {
                    emitter.next(Authentication.build(token.user?.name))
                    emitter.complete()
                }
            } else {
                log.info("Refresh token not found")
                emitter.error(
                    OauthErrorResponseException(
                        IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                        "Refresh token not found",
                        null
                    )
                )
            }
        }.onBackpressureBuffer()

    }


}