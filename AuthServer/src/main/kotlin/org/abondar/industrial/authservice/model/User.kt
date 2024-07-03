package org.abondar.industrial.authservice.model

import io.micronaut.core.annotation.NonNull
import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Relation
import io.micronaut.data.annotation.Relation.Cascade

@MappedEntity
data class User (

    @field:Id
    @field:GeneratedValue
    var id: Long? = null,

    @NonNull
    var name: String,

    @NonNull
    var password: String,

    @field:Relation(value = Relation.Kind.ONE_TO_MANY, cascade = [Cascade.ALL], mappedBy = "user")
    val token: List<RefreshToken> = emptyList()
)