package org.abondar.industrial.authservice.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule


@Controller("/v1/auth")
class AuthController {

    @Post
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun jwtLogin(): HttpResponse<String> {
        return HttpResponse.ok("Service is logged in")
    }


}