package org.abondar.industrial.authservice

import io.micronaut.runtime.Micronaut.run
import io.swagger.v3.oas.annotations.*
import io.swagger.v3.oas.annotations.info.*

@OpenAPIDefinition(
    info = Info(
        title = "Authentication Server",
        description = "Authentication Server API for usage in microservices",
        version = "1.0",
        license = License(name="MIT")
    )
)
object Api {
}

fun main(args: Array<String>) {
    run(*args)
}

