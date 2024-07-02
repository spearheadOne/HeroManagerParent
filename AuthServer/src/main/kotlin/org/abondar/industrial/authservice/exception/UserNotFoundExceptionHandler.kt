package org.abondar.industrial.authservice.exception


import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton


@Produces
@Singleton
class UserNotFoundExceptionHandler: ExceptionHandler<UserNotFoundException, HttpResponse<*>> {

    override fun handle(request: HttpRequest<*>?, exception: UserNotFoundException?): HttpResponse<*> {
        return HttpResponse.status<Any>(HttpStatus.NOT_FOUND).body(exception?.message)
    }

}