package org.abondar.industrial.authservice.controller

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.abondar.industrial.authservice.auth.DecodeUtil.Companion.CREDENTIALS_HEADER
import org.abondar.industrial.authservice.model.UserResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@MicronautTest
class AuthControllerTest(@Client("/") val client: HttpClient){

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `test auth by jwt`() {
        val creds = UsernamePasswordCredentials("test", "test")
        val credentials = creds.username + ":" + creds.password
        val encodedCredentials = Base64.encode(credentials.toByteArray())

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", null)
            .header(CREDENTIALS_HEADER, encodedCredentials)
            .accept("application/json")
        client.toBlocking().exchange(request, UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/login", creds)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest, BearerAccessRefreshToken::class.java)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()

        val authRequest: HttpRequest<*> = HttpRequest.POST("/v1/auth/",null)
            .accept("application/json")
            .bearerAuth(bearerAccessRefreshToken.accessToken)

        val findResponse: HttpResponse<String> = client.toBlocking().exchange(authRequest, String::class.java)
        assertEquals(HttpStatus.OK, findResponse.status)
        assertEquals("Service is logged in",findResponse.body())
    }
}