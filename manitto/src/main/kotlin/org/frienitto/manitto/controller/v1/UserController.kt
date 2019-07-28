package org.frienitto.manitto.controller.v1

import org.frienitto.manitto.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 인증이 필요한 모든 Api에는 Controller에 Parameter로 @RequestHeader(name = "X-Authorization") token: String 가 필수 입니다.
 * (파라미터명과 타입이 분명해야함 (token: String))
 */
@RestController
@RequestMapping(value = ["/api/v1"])
class UserController(private val userService: UserService)
