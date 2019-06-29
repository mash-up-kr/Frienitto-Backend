package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.dto.RoomResponse
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.Participant
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.dto.RoomRequest
import org.frienitto.manitto.repository.RoomRepository
import org.springframework.stereotype.Service

@Service
class RoomService(private val roomRepository: RoomRepository, private val userRoomMapService: UserRoomMapService) {

    fun createRoom(owner: User, request: RoomRequest): Response<RoomResponse> {
        val room = Room.newRoom(owner, request.name, request.code, request.expiresDate)
        roomRepository.save(room)
        val participant = Participant(owner.id, owner.username, owner.imageCode)
        val participantList: List<Participant> = listOf(participant)
        return Response(201, "방 생성 완료", RoomResponse(
                null, room.title, room.expiresDate, "", participantList))
    }

    fun getAllRoom(): Response<List<Room>> {
        val rooms = roomRepository.findAll()
        return Response(201, "방 생성 완료", rooms)
    }

    fun getRoomDetail(room_id:Long): Response<RoomResponse>{
        val room = roomRepository.findById(room_id).get() // 없을 때 예외 처리 필요
        val participants = userRoomMapService.findUsersByRoomId(room_id)
        return Response(200, "조회 성공", RoomResponse(
               room.id, room.title, room.expiresDate, room.url, participants))
    }



}