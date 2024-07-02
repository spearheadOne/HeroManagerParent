package org.abondar.industrial.authservice.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotNull



@Serdeable
data class UserUpdateRequest (
    @NotNull
    @JsonProperty(required = true)
    val id: Long,

    @NotNull
    @JsonProperty(required = true)
    val username: String,

    @NotNull
    @JsonProperty(required = true)
    val password: String,
)