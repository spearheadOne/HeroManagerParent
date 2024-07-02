package org.abondar.industrial.authservice.controller

import com.nimbusds.jwt.JWTParser
import com.nimbusds.jwt.SignedJWT
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.render.BearerAccessRefreshToken
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.abondar.industrial.authservice.model.UserResponse
import org.abondar.industrial.authservice.model.UserUpdateRequest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class UserControllerTest (@Client("/") val client: HttpClient){


    @Test
    fun `test create user`() {
        val credentials = UsernamePasswordCredentials("test","test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept("application/json")

        val resp : HttpResponse<UserResponse> = client.toBlocking().exchange(request,UserResponse::class.java)
        assertEquals(HttpStatus.CREATED, resp.status)

        val body = resp.body()
        assertNotNull(body.result)
    }

    @Test
    fun `test login user`() {
        val credentials = UsernamePasswordCredentials("test","test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept("application/json")
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", credentials)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest,BearerAccessRefreshToken::class.java)
        assertEquals(HttpStatus.OK, loginResponse.status)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()
        assertEquals("test", bearerAccessRefreshToken.username)
        assertNotNull(bearerAccessRefreshToken.accessToken)
        assertTrue(JWTParser.parse(bearerAccessRefreshToken.accessToken) is SignedJWT)
    }


    @Test
    fun `test login user wrong password`() {
        val credentials = UsernamePasswordCredentials("test","test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept("application/json")
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", UsernamePasswordCredentials("test", "testWrong"))
        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking()
                .exchange(loginRequest,BearerAccessRefreshToken::class.java)
        }
        assertEquals(HttpStatus.UNAUTHORIZED, ex.status)
    }


    @Test
    fun `test login user user not found`() {
        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", UsernamePasswordCredentials("test", "testWrong"))
        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking()
                .exchange(loginRequest,BearerAccessRefreshToken::class.java)
        }
        assertEquals(HttpStatus.UNAUTHORIZED, ex.status)
    }


    @Test
    fun `test update user`() {
        val credentials = UsernamePasswordCredentials("testUP","testUP")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept("application/json")
        val response : HttpResponse<UserResponse> = client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", credentials)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest,BearerAccessRefreshToken::class.java)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()

        val updateRequest: HttpRequest<*> = HttpRequest.PUT("/v1/user/",UserUpdateRequest(
            response.body().result.toLong(), "test1", "test1"))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .accept("application/json")
            .bearerAuth(bearerAccessRefreshToken.accessToken)


        val updateResponse: HttpResponse<UserResponse> = client.toBlocking().exchange(updateRequest,UserResponse::class.java)
        assertEquals(HttpStatus.OK, updateResponse.status)
        assertEquals("User updated successfully",updateResponse.body().result)


        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking()
                .exchange(loginRequest,BearerAccessRefreshToken::class.java)
        }
        assertEquals(HttpStatus.UNAUTHORIZED, ex.status)


        val updateloginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login",
            UsernamePasswordCredentials("test1", "test1"))

        val updLoginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(updateloginRequest,BearerAccessRefreshToken::class.java)
        assertEquals(HttpStatus.OK, updLoginResponse.status)
    }


    @Test
    fun `test find user`() {
        val credentials = UsernamePasswordCredentials("test", "test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .accept("application/json")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", credentials)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest,BearerAccessRefreshToken::class.java)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()

        val findRequest: HttpRequest<*> = HttpRequest.GET<Any>("/v1/user/"+credentials.username)
            .accept("application/json")
            .bearerAuth(bearerAccessRefreshToken.accessToken)

        val findResponse: HttpResponse<UserResponse> = client.toBlocking().exchange(findRequest,UserResponse::class.java)
        assertEquals(HttpStatus.OK, findResponse.status)
        assertEquals(credentials.username,findResponse.body().result)
    }


    @Test
    fun `test user not found`() {
        val credentials = UsernamePasswordCredentials("test", "test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .accept("application/json")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", credentials)
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

    @Test
    fun `test user unauthorized`() {
        val credentials =  UsernamePasswordCredentials("test", "test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .accept("application/json")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        client.toBlocking().exchange(request,UserResponse::class.java)


        val findRequest: HttpRequest<*> = HttpRequest.GET<Any>("/v1/user/user123")
            .accept("application/json")


        val ex = assertThrows(HttpClientResponseException::class.java) {
            client.toBlocking().exchange(findRequest,UserResponse::class.java)
        }

        assertEquals(HttpStatus.UNAUTHORIZED, ex.status)
    }

    @Test
    fun `test delete user`() {
        val credentials =  UsernamePasswordCredentials("test", "test")

        val request: HttpRequest<*> = HttpRequest.POST("/v1/user", credentials)
            .accept("application/json")
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        client.toBlocking().exchange(request,UserResponse::class.java)

        val loginRequest: HttpRequest<*> = HttpRequest.POST("/v1/user/login", credentials)
        val loginResponse: HttpResponse<BearerAccessRefreshToken> = client.toBlocking()
            .exchange(loginRequest,BearerAccessRefreshToken::class.java)

        val bearerAccessRefreshToken: BearerAccessRefreshToken = loginResponse.body()

        val deleteRequest: HttpRequest<*> = HttpRequest.DELETE<Any>("/v1/user/"+credentials.username)
            .accept("application/json")
            .bearerAuth(bearerAccessRefreshToken.accessToken)

        val deleteResponse: HttpResponse<UserResponse> = client.toBlocking().exchange(deleteRequest,UserResponse::class.java)
        assertEquals(HttpStatus.OK, deleteResponse.status)
        assertEquals("User deleted successfully",deleteResponse.body().result)
    }

}