package org.frienitto.manitto.controller.v1

import org.frienitto.manitto.dto.MatchRequest
import org.frienitto.manitto.dto.MatchResultDto
import org.frienitto.manitto.dto.MissionDto
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.service.MissionService
import org.frienitto.manitto.service.UserRoomMapService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class MatchingController(private val userRoomMapService: UserRoomMapService, private val missionService: MissionService) {

    @PostMapping("/matching")
    fun match(@RequestHeader(name = "X-Authorization") token: String, @RequestBody body: MatchRequest): Response<MatchResultDto> {
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, userRoomMapService.match(body))
    }

    @GetMapping("/matching-info/room/{id}")
    fun getUserMatchingInfo(@RequestHeader(name = "X-Authorization") token: String, @PathVariable(name = "id") roomId: Long): Response<List<MissionDto>> {
        val result = missionService.getUserMissionsByRoomId(roomId)
                .asSequence()
                .map { MissionDto.from(it) }
                .toList()

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, result)
    }
}