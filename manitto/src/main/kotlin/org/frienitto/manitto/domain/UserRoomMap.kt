package org.frienitto.manitto.domain

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user_room_maps")
class UserRoomMap private constructor(
        room: Room,
        user: User,
        username: String,
        imageCode: Int
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set(value) {
            field = value
        }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var room: Room = room
        private set(value) {
            field = value
        }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = ForeignKey(ConstraintMode.NO_CONSTRAINT))
    var user: User = user
        private set(value) {
            field = value
        }
    var username: String = username
        private set(value) {
            field = value
        }
    var imageCode: Int = imageCode
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is UserRoomMap) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object {
        fun newUserRoomMap(room: Room, user: User): UserRoomMap {
            return UserRoomMap(room, user, user.username, user.imageCode)
        }
    }
}
