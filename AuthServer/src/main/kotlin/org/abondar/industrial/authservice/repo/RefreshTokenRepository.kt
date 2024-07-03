package org.abondar.industrial.authservice.repo

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import jakarta.transaction.Transactional
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.abondar.industrial.authservice.model.RefreshToken
import java.time.Instant
import java.util.*

@JdbcRepository(dialect = Dialect.MYSQL)
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long>{

   fun updateRevokedByUsername( @NotNull username: String, isRevoked: Boolean): Long


   fun findByToken(@NotNull @NotBlank token: String): Optional<RefreshToken>

   @Transactional
   fun save(@NonNull @NotBlank username:  String,
            @NonNull @NotBlank token: String ,
            @NonNull @NotNull  isRevoked: Boolean,
            @NonNull @NotNull dateCreated: Instant): RefreshToken
}