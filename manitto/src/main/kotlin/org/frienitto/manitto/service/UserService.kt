package org.frienitto.manitto.service

import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.AccessTokenWithUserInfo
import org.frienitto.manitto.dto.SignInDto
import org.frienitto.manitto.dto.SignUpDto
import org.frienitto.manitto.dto.UserDto
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(private val userRepository: UserRepository) {

    @Transactional(readOnly = true)
    fun getUserByToken(userToken: String): User {
        return userRepository.findByToken(userToken) ?: throw NonAuthorizationException(errorMsg = "인증되지 않은 사용자 입니다.")
    }

    @Transactional
    fun signUp(signUpDto: SignUpDto): UserDto {
        val user = User.newUser(username = signUpDto.username,
                description = signUpDto.description,
                imageCode = signUpDto.imageCode,
                email = signUpDto.email,
                password = signUpDto.password)

        userRepository.save(user)
        return UserDto.from(user)
    }

    fun signIn(signInDto: SignInDto): AccessTokenWithUserInfo {
        val user = userRepository.findByEmail(signInDto.email) ?: throw ResourceNotFoundException(errorMsg = "찾을 수 없는 사용자 입니다.")

        if (user.password != signInDto.password) {
            throw NonAuthorizationException(errorMsg = "찾을 수 없는 사용자 입니다.")
        }
        return AccessTokenWithUserInfo(user.token, user.tokenExpiresDate, UserDto.from(user))
    }
}