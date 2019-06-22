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
        type: MissionType,
        status: MissionStatus,
        description: String
) {

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
    var createdBy: String? = null
        private set(value) {
            field = value
        }
    lateinit var updatedAt: LocalDateTime
    var updatedBy: String? = null
        private set(value) {
            field = value
        }

    @PrePersist
    fun onPersist() {
        val now = LocalDateTime.now()
        this.createdAt = now
        this.updatedAt = now
    }
}