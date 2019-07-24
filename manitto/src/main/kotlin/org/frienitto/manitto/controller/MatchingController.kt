package org.frienitto.manitto.controller

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
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
@Api(value = "마니또 매칭 Controller", description = "매칭이 완료된 방의 매칭 상세 정보 API")
class MatchingController(private val userRoomMapService: UserRoomMapService, private val missionService: MissionService) {

    @ApiOperation(value = "매칭 시작 API")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successfully match people")])
    @PostMapping("/matching")
    fun match(@RequestBody body: MatchRequest): Response<MatchResultDto> {
        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, userRoomMapService.match(body))
    }

    @ApiOperation(value = "매칭 정보 가져오는 API")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Successfully get matching info")])
    @GetMapping("/matching-info/room/{id}")
    fun getUserMatchingInfo(@PathVariable(name = "id") roomId: Long): Response<List<MissionDto>> {
        val result = missionService.getUserMissionsByRoomId(roomId)
                .asSequence()
                .map { MissionDto.from(it) }
                .toList()

        return Response(HttpStatus.OK.value(), HttpStatus.OK.reasonPhrase, result)
    }
}