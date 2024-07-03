package org.abondar.industrial.authservice.auth

import io.micronaut.security.authentication.Authentication
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode
import io.micronaut.security.errors.OauthErrorResponseException
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent
import io.micronaut.security.token.refresh.RefreshTokenPersistence
import jakarta.inject.Singleton
import org.abondar.industrial.authservice.model.RefreshToken
import org.abondar.industrial.authservice.repo.RefreshTokenRepository
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import java.time.Instant
import java.util.*

@Singleton
class AuthServiceRefreshTokenPersistence(
    private val refreshTokenRepository: RefreshTokenRepository
) : RefreshTokenPersistence {


    override fun persistToken(event: RefreshTokenGeneratedEvent?) {
        if (event?.refreshToken != null && event.authentication?.name != null) {
            refreshTokenRepository.save(event.authentication.name,event.refreshToken,false, Instant.now())
        }
    }

    override fun getAuthentication(refreshToken: String?): Publisher<Authentication> {
        return Flux.create { emitter ->

            val nonNullableRefreshToken = refreshToken ?: run {
                emitter.error(
                    OauthErrorResponseException(
                        IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                        "refresh token is null",
                        null
                    )
                )
                return@create
            }

            val tokenOpt: Optional<RefreshToken> = refreshTokenRepository.findByToken(nonNullableRefreshToken)
            if (tokenOpt.isPresent) {
                val token = tokenOpt.get()
                if (token.isRevoked) {
                    emitter.error(
                        OauthErrorResponseException(
                            IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                            "refresh token revoked",
                            null
                        )
                    )
                } else {
                    emitter.next(Authentication.build(token.username))
                    emitter.complete()
                }
            } else {
                emitter.error(
                    OauthErrorResponseException(
                        IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                        "refresh token not found",
                        null
                    )
                )
            }
        }.onBackpressureBuffer()

    }


}