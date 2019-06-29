package org.frienitto.manitto.repository

import org.frienitto.manitto.domain.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long?> {

    fun findByNickname(nickname: String): User?
    fun findByToken(token: String): User?
}