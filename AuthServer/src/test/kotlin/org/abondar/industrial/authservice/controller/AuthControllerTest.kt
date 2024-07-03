package org.abondar.industrial.authservice.controller

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.abondar.industrial.authservice.model.UserResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.URLEncoder

@MicronautTest
class AuthControllerTest(@Client("/") val client: HttpClient){

    @Test
    fun `test auth by jwt`() {
        val credentials = UsernamePasswordCredentials("test", "test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .accept("application/json")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        client.toBlocking().exchange(request, UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", credentials)
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

    @Test
    fun `test refresh`() {
        val credentials = UsernamePasswordCredentials("test", "test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .accept("application/json")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        client.toBlocking().exchange(request, UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", credentials)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest, BearerAccessRefreshToken::class.java)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()
        val encodedBody = "grant_type=refresh_token&refresh_token=${URLEncoder.encode(bearerAccessRefreshToken.refreshToken, "UTF-8")}"

        val refreshRequest: HttpRequest<*> = HttpRequest.POST("/v1/auth/refresh",encodedBody)

            .contentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE)

        val refreshResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(refreshRequest, BearerAccessRefreshToken::class.java)

        val refreshedToken: BearerAccessRefreshToken = refreshResponse.body()
        assertEquals("test", refreshedToken.username)
        assertNotNull(refreshedToken.accessToken)
        assertTrue(JWTParser.parse(refreshedToken.accessToken) is SignedJWT)


    }
}