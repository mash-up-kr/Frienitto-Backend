package org.frienitto.manitto.controller.v1

import org.frienitto.manitto.service.UserService
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(value = ["/api/v1"])
class UserController(private val userService: UserService)
