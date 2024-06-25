package org.abondar.industrial.authservice.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull

data class UserResponse(
    @NotNull
    @JsonProperty(required = true)
    val result: String,

)