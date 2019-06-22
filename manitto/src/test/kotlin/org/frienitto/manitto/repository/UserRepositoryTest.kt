package org.frienitto.manitto.repository

import org.frienitto.manitto.SpringTestSupport
import org.frienitto.manitto.domain.User
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTest : SpringTestSupport() {

    @Autowired
    lateinit var userRepository: UserRepository

    companion object {
        private val NICKNAME: String = "코딩하는 오징어"
        private val EMAIL: String = "codingsquid@gmail.com"
        private val PASSWORD: String = "1234"
    }

    @BeforeEach
    fun setUp() {
        userRepository.save(mockUser(nickname = NICKNAME, email = EMAIL, password = PASSWORD))
    }

    @AfterEach
    fun tearDown() {
        userRepository.deleteAll()
    }

    @Test
    fun `유저 닉네임 조회`() {
        val result  = userRepository.findByNickname(NICKNAME) ?: mockUser("FAIL", "FAIL", "FAIL", "FAIL")
        assertEquals(NICKNAME, result.nickname)
        assertEquals(EMAIL, result.email)
    }

    private fun mockUser(nickname: String, description: String = "hello manitto", email: String, password: String): User {
        return User.newUser(nickname, description, email, password)
    }
}