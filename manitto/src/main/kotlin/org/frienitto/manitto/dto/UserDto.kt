package org.frienitto.manitto.dto

import org.frienitto.manitto.domain.User

data class SignUpDto(val username: String, val description: String, val imageCode: Int, val email: String, val password: String)

data class SignInDto(val email: String, val password: String)

data class UserDto(val username: String, val description: String, val imageCode: Int, val email: String) {

    companion object {
        fun from(user: User): UserDto {
            return UserDto(user.username, user.description, user.imageCode, user.email)
        }
    }
}