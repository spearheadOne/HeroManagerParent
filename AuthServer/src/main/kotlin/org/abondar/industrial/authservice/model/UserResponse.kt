package org.abondar.industrial.authservice.model

import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable
import jakarta.validation.constraints.NotNull

@Serdeable
data class UserResponse(
    @NotNull
    @JsonProperty(required = true)
    val result: String,

)