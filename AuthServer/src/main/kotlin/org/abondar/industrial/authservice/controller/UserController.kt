package org.abondar.industrial.authservice.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.rules.SecurityRule
import jakarta.inject.Inject
import org.abondar.industrial.authservice.model.User
import org.abondar.industrial.authservice.model.UserResponse
import org.abondar.industrial.authservice.model.UserUpdateRequest
import org.abondar.industrial.authservice.service.UserService


@Controller("/v1/user")
class UserController @Inject constructor(
    private val userService: UserService,

) {

    @Post
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun register(@Body userCreateRequest: UsernamePasswordCredentials): HttpResponse<UserResponse> {
        val user = User(null,userCreateRequest.username,userCreateRequest.password)
        val resp = userService.createUser(user)
        return HttpResponse.created(resp)
    }

    @Put
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun update(@Body body: UserUpdateRequest): HttpResponse<UserResponse> {
        val user = User(
            body.id,
            body.username,
            body.password,
        )

        val resp = userService.updateUser(user)
        return HttpResponse.ok(resp)
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