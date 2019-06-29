package org.frienitto.manitto.service

import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.repository.UserRepository
import org.frienitto.manitto.util.generateAuthCode
import org.frienitto.manitto.util.generateToken
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.HashSet

@Service
class AuthService(private val emailService: EmailService, private val userRepository: UserRepository) {

    //TODO Redis를 사용하기 전 임시로 사용할 컨테이너 Scale out 할 경우 제거 후 Redis를 이용해야 합니다. (좀 더 추상화 할 수 없을까??)
    //TODO 전부 다 Redis사용해야함
    private val authCodeMap: MutableMap<String, String> = ConcurrentHashMap()
    private val tokenSet: MutableSet<String> = Collections.synchronizedSet(setOf("TESTTOKEN"))

    companion object {
        private val log = LoggerFactory.getLogger(AuthService::class.java)
    }

    fun sendAuthCodeToEmail(to: String) {
        val key = to + System.currentTimeMillis().toString()
        val code = generateAuthCode(key)
        val registerToken = generateToken(key, 32)

        authCodeMap[code] = registerToken
        tokenSet.add(registerToken)
        emailService.send("프렌또 가입 본인인증", "인증 코드 : $code", to)
    }

    fun isRegisterable(token: String): Boolean {
        return tokenSet.contains(token)
    }

    //TODO 캐싱이 들어가면 좋을 것 같습니다.
    fun isAuth(token: String): Boolean {
        return userRepository.findByToken(token) != null
    }

    fun verifyCode(code: String): String {
        return authCodeMap[code] ?: throw NonAuthorizationException()
    }
}