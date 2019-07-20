package org.frienitto.manitto.domain.constant

enum class MissionType {
    ROOM,
    USER;

    fun isUserMission(): Boolean {
        return this == USER
    }

    fun isRoomMission(): Boolean {
        return !isUserMission()
    }
}