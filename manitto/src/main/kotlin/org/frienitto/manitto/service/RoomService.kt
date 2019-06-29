package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.RoomResponse
import org.frienitto.manitto.dto.Response
import org.frienitto.manitto.repository.RoomRepository
import org.springframework.stereotype.Service

@Service
class RoomService(private val roomRepository: RoomRepository){

//    roomRepository
//    val createRoom: Response<RoomResponse> = Response(201, "방 생성 완료", RoomResponse(
//            null, room.title, room.expiresDate, "", room.participants))
//    fun createRoom(room: Room): Response<RoomResponse> {
//        roomRepository.save(room)
//        val createRoom: Response<RoomResponse> = Response(201, "방 생성 완료", RoomResponse(
//            null, room.title, room.expiresDate, "",  //TODO getPariticents))
//    return Response(201, "방 생성 완료",RoomResponse(
//       null
//
//    ));

    }

    fun joinRoom(){

    }
}