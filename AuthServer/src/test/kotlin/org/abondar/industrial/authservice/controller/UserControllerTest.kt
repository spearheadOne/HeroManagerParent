package org.abondar.industrial.authservice.controller

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpHeaders
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.abondar.industrial.authservice.model.UserResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@MicronautTest
class UserControllerTest (@Client("/") val client: HttpClient){


    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `test create user`() {
        val credentials = "test:test"
        val encodedCredentials = Base64.encode(credentials.toByteArray())

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", null)
            .header(HttpHeaders.AUTHORIZATION, encodedCredentials)
            .accept("application/json")

        val resp : HttpResponse<UserResponse> = client.toBlocking().exchange(request,UserResponse::class.java)
        assertEquals(HttpStatus.CREATED, resp.status)

        val body = resp.body()
        assertTrue(body.result.contains("User created successfully with id"))
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `test login user`() {
        val creds = UsernamePasswordCredentials("test", "test")
        val credentials = creds.username + ":" + creds.password
        val encodedCredentials = Base64.encode(credentials.toByteArray())

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", null)
            .header(HttpHeaders.AUTHORIZATION, encodedCredentials)
            .accept("application/json")
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/login", creds)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest,BearerAccessRefreshToken::class.java)
        assertEquals(HttpStatus.OK, loginResponse.status)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()
        assertEquals("test", bearerAccessRefreshToken.username)
        assertNotNull(bearerAccessRefreshToken.accessToken)
        assertTrue(JWTParser.parse(bearerAccessRefreshToken.accessToken) is SignedJWT)
    }


    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `test login user wrong password`() {
        val credentials = "test:test"
        val encodedCredentials = Base64.encode(credentials.toByteArray())

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", null)
            .header(HttpHeaders.AUTHORIZATION, encodedCredentials)
            .accept("application/json")
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/login", UsernamePasswordCredentials("test", "testWrong"))
        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking()
                .exchange(loginRequest,BearerAccessRefreshToken::class.java)
        }
        assertEquals(HttpStatus.UNAUTHORIZED, ex.status)
    }


    @Test
    fun `test login user user not found`() {
        val loginRequest: HttpRequest<*> = HttpRequest.POST("/login", UsernamePasswordCredentials("test", "testWrong"))
        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking()
                .exchange(loginRequest,BearerAccessRefreshToken::class.java)
        }
        assertEquals(HttpStatus.UNAUTHORIZED, ex.status)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `test find user`() {
        val creds = UsernamePasswordCredentials("test", "test")
        val credentials = creds.username + ":" + creds.password
        val encodedCredentials = Base64.encode(credentials.toByteArray())

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", null)
            .header(HttpHeaders.AUTHORIZATION, encodedCredentials)
            .accept("application/json")
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/login", creds)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest,BearerAccessRefreshToken::class.java)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()

        val findRequest: HttpRequest<*> = HttpRequest.GET<Any>("/v1/user/"+creds.username)
            .accept("application/json")
            .bearerAuth(bearerAccessRefreshToken.accessToken)

        val findResponse: HttpResponse<UserResponse> = client.toBlocking().exchange(findRequest,UserResponse::class.java)
        assertEquals(HttpStatus.OK, findResponse.status)
        assertEquals(creds.username,findResponse.body().result)
    }


    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `test user not found`() {
        val creds = UsernamePasswordCredentials("test", "test")
        val credentials = creds.username + ":" + creds.password
        val encodedCredentials = Base64.encode(credentials.toByteArray())

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", null)
            .header(HttpHeaders.AUTHORIZATION, encodedCredentials)
            .accept("application/json")
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/login", creds)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest,BearerAccessRefreshToken::class.java)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()

        val findRequest: HttpRequest<*> = HttpRequest.GET<Any>("/v1/user/user123")
            .accept("application/json")
            .bearerAuth(bearerAccessRefreshToken.accessToken)


        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(findRequest,UserResponse::class.java)
        }

        assertEquals(HttpStatus.NOT_FOUND, ex.status)
        assertNotNull(ex.message)
    }

    @OptIn(ExperimentalEncodingApi::class)
    @Test
    fun `test delete user`() {
        val creds = UsernamePasswordCredentials("test", "test")
        val credentials = creds.username + ":" + creds.password
        val encodedCredentials = Base64.encode(credentials.toByteArray())

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", null)
            .header(HttpHeaders.AUTHORIZATION, encodedCredentials)
            .accept("application/json")
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/login", creds)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest,BearerAccessRefreshToken::class.java)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()

        val deleteRequest: HttpRequest<*> = HttpRequest.DELETE<Any>("/v1/user/"+creds.username)
            .accept("application/json")
            .bearerAuth(bearerAccessRefreshToken.accessToken)

        val deleteResponse: HttpResponse<UserResponse> = client.toBlocking().exchange(deleteRequest,UserResponse::class.java)
        assertEquals(HttpStatus.OK, deleteResponse.status)
        assertEquals("User deleted successfully",deleteResponse.body().result)
    }

}