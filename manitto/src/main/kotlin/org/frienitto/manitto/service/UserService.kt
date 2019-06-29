package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.Participant
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.dto.RoomRequest
import org.frienitto.manitto.dto.RoomResponse
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

    fun getUserByToken(userToken: String): User {
        return userRepository.findByToken(userToken) ?: throw NonAuthorizationException()
    }
}