package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Room
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.dto.RoomCreateRequest
import org.frienitto.manitto.dto.RoomDto
import org.frienitto.manitto.dto.RoomJoinByTitleRequest
import org.frienitto.manitto.dto.RoomRetrieveRequest
import org.frienitto.manitto.exception.BadRequestException
import org.frienitto.manitto.exception.DuplicateDataException
import org.frienitto.manitto.exception.NonAuthorizationException
import org.frienitto.manitto.exception.ResourceNotFoundException
import org.frienitto.manitto.repository.RoomRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RoomService(private val roomRepository: RoomRepository,
                  private val userRoomMapService: UserRoomMapService,
                  private val userService: UserService) {

    @Transactional
    fun createRoom(owner: User, createRequest: RoomCreateRequest): RoomDto {
        roomRepository.findByTitle(createRequest.title)?.let {
            throw DuplicateDataException(errorMsg = "이미 존재하는 방 제목 입니다.")
        }

        val room = save(Room.newRoom(owner, createRequest.title, createRequest.code, createRequest.expiresDate))
        joinRoomById(owner, room.id!!, room.code)

        return RoomDto.from(room)
    }

    @Transactional
    fun deleteRoom(user: User, title: String) {
        val room = roomRepository.findByTitle(title) ?: throw ResourceNotFoundException(errorMsg = "삭제 가능한 방이 없습니다.")

        if (room.validateOwner(user)) {
            userRoomMapService.disconnectAllByRoomId(room.id!!)
            roomRepository.delete(room)
            return
        }

        userRoomMapService.disconnect(user.id!!, room.id!!)
    }

    @Transactional
    fun joinRoomByTitle(user: User, byTitleRequest: RoomJoinByTitleRequest): RoomDto {
        val room = roomRepository.findByTitle(byTitleRequest.title) ?: throw ResourceNotFoundException(errorMsg = "요청한 방을 찾을 수 없습니다.")

        if (room.code != byTitleRequest.code) {
            throw NonAuthorizationException(errorCode = 405, errorMsg = "방 생성 코드가 맞지 않음")
        }

        userRoomMapService.connect(user, room)

        return RoomDto.from(room)
    }

    @Transactional
    fun joinRoomById(user: User, roomId: Long, code: String): RoomDto {
        val room = roomRepository.findById(roomId).orElseThrow { ResourceNotFoundException(errorMsg = "요청 방을 찾을 수 없음") }

        if (room.code != code) {
            throw NonAuthorizationException(errorCode = 405, errorMsg = "방 생성 코드가 맞지 않음")
        }

        userRoomMapService.connect(user, room)

        return RoomDto.from(room)
    }

    //TODO 페이징 처리 해야함
    @Transactional(readOnly = true)
    fun getRoomList(user: User): List<Room> {
        return roomRepository.findAll()
    }

    @Transactional(readOnly = true)
    fun getRoomInfoWithValidateOwner(retrieveRequest: RoomRetrieveRequest): RoomDto {
        val room = when {
            retrieveRequest.title != null -> roomRepository.findByTitle(retrieveRequest.title) ?: throw ResourceNotFoundException(errorMsg = "해당 요청하는 방을 찾을 수 없습니다.")
            retrieveRequest.roomId != null -> roomRepository.findById(retrieveRequest.roomId).orElseThrow { ResourceNotFoundException(errorMsg = "해당 요청하는 방을 찾을 수 없습니다.") }
            else -> throw BadRequestException()
        }

        val participants = userRoomMapService.getParticipantsByRoomId(room.id!!)
        val requestUser = userService.getUserByToken(retrieveRequest.userToken)

        return RoomDto.from(room = room, isOwner = room.validateOwner(requestUser), participants = participants)
    }

    @Transactional(readOnly = true)
    fun getRoomById(roomId: Long): Room {
        return roomRepository.findById(roomId).orElseThrow { ResourceNotFoundException(errorMsg = "해당 요청하는 방을 찾을 수 없습니다.") }
    }

    @Transactional
    fun save(room: Room): Room {
        return roomRepository.save(room)
    }
}