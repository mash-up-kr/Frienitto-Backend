package org.frienitto.manitto.controller

import org.frienitto.manitto.dto.*
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.service.AuthService
import org.frienitto.manitto.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(value = ["/api/v1"])
class UserController(private val authService: AuthService, private val userService: UserService) {

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
