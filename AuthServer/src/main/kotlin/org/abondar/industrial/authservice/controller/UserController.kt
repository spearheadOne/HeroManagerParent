package org.abondar.industrial.authservice.controller

import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject
import org.abondar.industrial.authservice.auth.DecodeUtil.Companion.decodeCredentials
import org.abondar.industrial.authservice.model.UserResponse
import org.abondar.industrial.authservice.service.UserService


@Controller("/v1/user")
class UserController @Inject constructor(
    private val userService: UserService,

) {

    @Post
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun register(@Header(HttpHeaders.AUTHORIZATION) authHeader: String): HttpResponse<UserResponse> {
        val user = decodeCredentials(authHeader)
        val resp = userService.createUser(user)
        return HttpResponse.created(resp)
    }

    @Get("/{name}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun find( @PathVariable name: String): HttpResponse<UserResponse> {

        val res = userService.findUser(name)
        return HttpResponse.ok(res)
    }

    @Delete("/{name}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun delete( @PathVariable name: String): HttpResponse<UserResponse> {
        val res = userService.deleteUser(name)
        return HttpResponse.ok(res)
    }


}