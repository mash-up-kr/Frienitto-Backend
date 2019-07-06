package org.frienitto.manitto.domain

import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_room_maps")
class UserRoomMap private constructor(
        roomId: Long,
        userId: Long,
        username: String,
        expiredDate: LocalDate,
        imageCode: Int
) {

    companion object{
        fun newUserRoomMap(roomId: Long, userId: Long, username: String, expiresDate: LocalDate, imageCode: Int): UserRoomMap {
            return UserRoomMap(roomId ,userId, username, expiresDate, imageCode)
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set(value) {
            field = value
        }
    var roomId: Long = roomId
        private set(value) {
            field = value
        }
    var userId: Long = userId
        private set(value) {
            field = value
        }
    var username: String = username
        private set(value) {
            field = value
        }

    var imageCode: Int = imageCode
        private set(value){
            field = value
        }

    var expiredDate: LocalDate = expiredDate
        private set(value) {
            field = value
        }
    lateinit var createdAt: LocalDateTime
    lateinit var updatedAt: LocalDateTime

    @PrePersist
    fun onPersist() {
        val now = LocalDateTime.now()
        this.createdAt = now
        this.updatedAt = now
    }

    @PreUpdate
    fun onUpdate() {
        this.updatedAt = LocalDateTime.now()
    }
}