package org.frienitto.manitto.controller.auth

import org.frienitto.manitto.dto.*
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.service.AuthService
import org.frienitto.manitto.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1"])
class AuthController(private val authService: AuthService, private val userService: UserService) {

    @PostMapping(value = ["/issue/code"])
    fun issueCode(@RequestBody body: IssueCodeRequest): Response<Unit> {
        authService.sendAuthCodeToEmail(body.receiverInfo)
        return Response(HttpStatus.ACCEPTED.value(), HttpStatus.ACCEPTED.reasonPhrase)
    }

    @PostMapping(value = ["/verify/code"])
    fun verifyCode(@RequestBody body: VerifyCodeRequest): Response<RegisterToken> {
        val registerToken = authService.verifyCode(body.code)
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, RegisterToken(registerToken))
    }

    @PostMapping(value = ["/sign-up"])
    fun signUp(@RequestHeader("X-Register-Token") registerToken: String, @RequestBody body: SignUpDto): Response<UserDto> {
        if (!authService.isRegisterable(registerToken)) {
            throw NonAuthorizationException()
        }
        val userDto = userService.signUp(body)

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, userDto)
    }

    @PostMapping(value = ["/sign-in"])
    fun signIn(@RequestBody body: SignInDto): Response<AccessToken> {
        val accessToken = userService.signIn(body)

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, accessToken)
    }
}