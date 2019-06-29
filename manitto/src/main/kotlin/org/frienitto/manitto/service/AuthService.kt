package org.frienitto.manitto.service

import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.repository.UserRepository
import org.frienitto.manitto.util.generateAuthCode
import org.frienitto.manitto.util.generateToken
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap

@Service
class AuthService(private val emailService: EmailService, private val userRepository: UserRepository) {

    //TODO Redis를 사용하기 전 임시로 사용할 컨테이너 Scale out 할 경우 제거 후 Redis를 이용해야 합니다. (좀 더 추상화 할 수 없을까??)
    private val authCodeMap: MutableMap<String, String> = ConcurrentHashMap()

    fun sendAuthCodeToEmail(to: String) {
        val key = to + System.currentTimeMillis().toString()
        val code = generateAuthCode(key)
        val registerToken = generateToken(key, 32)

        authCodeMap[code] = registerToken
        emailService.send("프렌또 가입 본인인증", "인증 코드 : $code", to)
    }

    fun isRegisterable(code: String, token: String): Boolean {
        val registerToken = authCodeMap[code] ?: throw NonAuthorizationException()
        return token == registerToken
    }

    //TODO 캐싱이 들어가면 좋을 것 같습니다.
    fun isAuth(token: String): Boolean {
        return userRepository.findByToken(token) != null
    }

    fun verifyCode(code: String): String {
        return authCodeMap[code] ?: throw NonAuthorizationException()
    }
}