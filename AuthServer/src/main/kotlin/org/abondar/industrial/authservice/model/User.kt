package org.abondar.industrial.authservice.model

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity

@MappedEntity
data class User (

    @field:Id
    @field:GeneratedValue
    var id: Long? = null,

    @NonNull
    var name: String,

    @NonNull
    var password: String,
)