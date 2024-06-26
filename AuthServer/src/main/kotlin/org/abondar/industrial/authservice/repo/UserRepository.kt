package org.abondar.industrial.authservice.repo

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.CrudRepository
import org.abondar.industrial.authservice.model.User
import java.util.*

@JdbcRepository(dialect = Dialect.MYSQL)
interface UserRepository : CrudRepository<User, Long> {


    fun findByName(username: String): Optional<User>

    fun deleteByName(username: String)
}