package org.frienitto.manitto.service

import org.frienitto.manitto.domain.Mission
import org.frienitto.manitto.domain.User
import org.frienitto.manitto.domain.UserRoomMap
import org.frienitto.manitto.domain.constant.MissionStatus
import org.frienitto.manitto.domain.constant.MissionType
import org.frienitto.manitto.dto.*
import org.frienitto.manitto.repository.MissionRepository
import org.frienitto.manitto.repository.RoomRepository
import org.frienitto.manitto.repository.UserRepository
import org.frienitto.manitto.repository.UserRoomMapRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.collections.ArrayList
import kotlin.streams.toList

@Service
class UserRoomMapService(private val usersRoomMapRepository: UserRoomMapRepository,
                         private val roomRepository: RoomRepository,
                         private val userRepository: UserRepository,
                         private val missionRepository: MissionRepository) {

    fun findUsersByRoomId(roomId: Long): List<Participant> {
        val userRoomMaps = usersRoomMapRepository.findByRoomId(roomId)
        return userRoomMaps.stream()
                .map {
                    Participant(it.id, it.username, it.imageCode)
                }.toList()
    }

    fun joinRoomsByRoomId(user: User, roomId: Long): Response<RoomResponse> {
        val room = roomRepository.findById(roomId).get() //예외 처리

        usersRoomMapRepository.save(UserRoomMap.joinRoom(room.id, user.id, user.username, room.expiresDate, user.imageCode))
        val userRoomMaps = usersRoomMapRepository.findByRoomId(roomId)
        val participants = userRoomMaps.stream()
                .map {
                    Participant(it.id, it.username, it.imageCode)
                }.toList()
        return Response(200, "방 입장 성공", RoomResponse(
                room.id, room.title, room.expiresDate, room.url, participants))
    }

    fun matchingStart(matchRequest: MatchRequest): Response<MatchResponse> {
        val participants = matchRequest.participants
        val a = participants.drop(1)
        val b = participants.take(participants.size - 1)
        val missions = mutableListOf<Mission>()
        for (x in 0 until participants.size) {
            missions.add(Mission.newMission(a[x], b[x], matchRequest.roomId, MissionType.USER, MissionStatus.DELIVERY, "Test"))
        }
        missionRepository.saveAll(missions)
        return Response(200, "매치 성공", MatchResponse(missions))
    }
}