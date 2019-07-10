package org.frienitto.manitto.controller

import org.frienitto.manitto.dto.MatchRequest
import org.frienitto.manitto.dto.MatchResultDto
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.service.UserRoomMapService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1")
class MatchingController(private val userRoomMapService: UserRoomMapService) {

    @PostMapping("matching")
    fun match(@RequestBody body: MatchRequest): Response<MatchResultDto> {
        return userRoomMapService.match(body)
    }
}