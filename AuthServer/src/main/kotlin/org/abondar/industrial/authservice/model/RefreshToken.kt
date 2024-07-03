package org.abondar.industrial.authservice.model

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.*
import io.micronaut.data.model.naming.NamingStrategies
import jakarta.validation.constraints.NotBlank
import java.time.Instant

@MappedEntity(value = "refresh_token", namingStrategy = NamingStrategies.UnderScoreSeparatedUpperCase::class)
data class RefreshToken (

    @field:Id
    @field:GeneratedValue
    var id: Long? = null,

    @NonNull
    @NotBlank
    var token: String,

    @NonNull
    @NotBlank
    var username: String,

    @NonNull
    @field:MappedProperty("is_revoked")
    var isRevoked: Boolean,

    @DateCreated
    @NonNull
    var dateCreated: Instant,

    )
