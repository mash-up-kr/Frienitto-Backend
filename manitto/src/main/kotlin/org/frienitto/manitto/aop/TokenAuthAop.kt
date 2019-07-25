package org.frienitto.manitto.aop

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.reflect.MethodSignature
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.service.AuthService
import org.springframework.stereotype.Component
import java.lang.reflect.Method

@Aspect
@Component
class TokenAuthAop(private val authService: AuthService) {

    @Before("execution(* org.frienitto.manitto.controller.v1.*.*(..))")
    fun verifyToken(joinPoint: JoinPoint) {
        val method: Method = (joinPoint.signature as MethodSignature).method
        lateinit var token: String

        method.parameters.forEachIndexed { index, parameter ->
            if (parameter.name == "token") {
                token = joinPoint.args[index] as String
            }
        }

        if (!authService.isAuth(token)) {
            throw NonAuthorizationException()
        }
    }
}