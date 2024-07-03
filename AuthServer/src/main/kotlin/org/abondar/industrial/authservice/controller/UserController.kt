package org.abondar.industrial.authservice.controller

import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.inject.Inject
import org.abondar.industrial.authservice.model.User
import org.abondar.industrial.authservice.model.UserResponse
import org.abondar.industrial.authservice.model.UserUpdateRequest
import org.abondar.industrial.authservice.service.UserService


@Controller("/v1/user")
@Tag(name = "Authentication", description = "Operations related to user management")
class UserController @Inject constructor(
    private val userService: UserService,

    ) {

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "201", description = "User is created")
        ]
    )
    @Post
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun register(@Body userCreateRequest: UsernamePasswordCredentials): HttpResponse<UserResponse> {
        val user = User(null, userCreateRequest.username, userCreateRequest.password)
        val resp = userService.createUser(user)
        return HttpResponse.created(resp)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User credentials are updated")
        ]
    )
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

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User is found"),
            ApiResponse(responseCode = "401", description = "User unauthorized"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    @Get("/{name}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun find(@PathVariable name: String): HttpResponse<UserResponse> {

        val res = userService.findUser(name)
        return HttpResponse.ok(res)
    }

    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "User is found"),
            ApiResponse(responseCode = "401", description = "User unauthorized"),
            ApiResponse(responseCode = "404", description = "User not found")
        ]
    )
    @Delete("/{name}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    fun delete(@PathVariable name: String): HttpResponse<UserResponse> {
        val res = userService.deleteUser(name)
        return HttpResponse.ok(res)
    }


}