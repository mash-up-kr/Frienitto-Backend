package org.frienitto.manitto.domain

import org.frienitto.manitto.domain.constant.MissionStatus
import org.frienitto.manitto.domain.constant.MissionType
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "missions")
class Mission private constructor(
        sourceId: Long,
        targetId: Long,
        roomId: Long?,
        type: MissionType,
        status: MissionStatus,
        description: String
) {
    companion object {
        fun newMission(sourceId: Long, targetId: Long, roomId: Long?, type: MissionType, status: MissionStatus, description: String): Mission {
            return Mission(sourceId, targetId, roomId, type, status, description).apply {
                this.sourceId = sourceId
                this.targetId = targetId
                this.roomId = roomId
                this.type = type
                this.status = status
                this.description = description
            }
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set(value) {
            field = value
        }
    var sourceId: Long = sourceId
        private set(value) {
            field = value
        }
    var targetId: Long = targetId
        private set(value) {
            field = value
        }
    var roomId: Long? = roomId
        private set(value) {
            field = value
        }
    @Enumerated(EnumType.STRING)
    var type: MissionType = type
        private set(value) {
            field = value
        }
    var description: String = description
        private set(value) {
            field = value
        }
    @Enumerated(EnumType.STRING)
    var status: MissionStatus = status
        private set(value) {
            field = value
        }
    lateinit var createdAt: LocalDateTime
    var createdBy: String = "system"
        private set(value) {
            field = value
        }
    lateinit var updatedAt: LocalDateTime
    var updatedBy: String = "system"
        private set(value) {
            field = value
        }

    @PrePersist
    fun onPersist() {
        val now = LocalDateTime.now()
        this.createdAt = now
        this.updatedAt = now
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Mission) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}