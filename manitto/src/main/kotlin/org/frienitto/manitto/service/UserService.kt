package org.frienitto.manitto.service

import org.frienitto.manitto.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val userRepository: UserRepository) {

}