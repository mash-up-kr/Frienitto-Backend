package org.frienitto.manitto.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "rooms")
class Room private constructor(
        owner: User,
        title: String,
        code: String,
        expiresDate: LocalDate
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set(value) {
            field = value
        }
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", foreignKey = ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    var owner: User = owner
        private set(value) {
            field = value
        }
    var title: String = title
        private set(value) {
            field = value
        }
    var code: String = code
        private set(value) {
            field = value
        }

    lateinit var createdAt: LocalDateTime
    var createdBy: String? = null
        private set(value) {
            field = value
        }
    var url: String? = null
        private set(value) {
            field = value
        }

    lateinit var expiresDate: LocalDate

    lateinit var updatedAt: LocalDateTime

    var updatedBy: String? = null
        private set(value) {
            field = value
        }

    @PrePersist
    fun onPersist() {
        val now = LocalDateTime.now()
        this.createdAt = now
        this.createdBy = this.createdBy ?: "system"
        this.updatedAt = now
        this.updatedBy = this.updatedBy ?: "system"
    }

    companion object {
        fun newRoom(owner: User, title: String, code: String, expiresDate: LocalDate): Room {
            return Room(owner, title, code, expiresDate).apply {
                this.createdBy = owner.nickname
                this.updatedBy = owner.nickname
                this.expiresDate = expiresDate
            }
        }
    }
}