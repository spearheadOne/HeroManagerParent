package org.abondar.industrial.authservice.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag


@Controller("/v1/auth")
@Tag(name = "JWT Authentication", description = "Operations related to microservice authentication")
class AuthController {


    @Operation(summary = "Authenticate a microservice", description = "Authenticate a service by provided JWT token. JWT token is obtained by /v1/usr/login. " +
            "Token is refresh by /v1/auth/refresh")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Service is authenticated "),
            ApiResponse(responseCode = "401", description = "Service unauthenticated")
        ]
    )
    @Post
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun jwtLogin(): HttpResponse<String> {
        return HttpResponse.ok("Service is logged in")
    }


}