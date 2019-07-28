package org.frienitto.manitto.domain

import org.frienitto.manitto.dto.AccessToken
import java.time.LocalDate
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "users")
class User private constructor(
        username: String,
        nickname: String?,
        description: String,
        imageCode: Int,
        email: String,
        password: String
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
        private set(value) {
            field = value
        }
    var username: String = username
        private set(value) {
            field = value
        }
    var nickname: String? = nickname
        private set(value) {
            field = value
        }
    var description: String = description
        private set(value) {
            field = value
        }
    var imageCode: Int = imageCode
        private set(value) {
            field = value
        }
    var email: String = email
        private set(value) {
            field = value
        }
    var password: String = password
        private set(value) {
            field = value
        }
    private lateinit var salt: String
    lateinit var token: String
    lateinit var tokenExpiresDate: LocalDate
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

    //TODO 비밀번호 + salt를 이용한 암호화는 PrePersist 이벤트에서 진행
    @PrePersist
    fun onPersist() {
        val now = LocalDateTime.now()
        val accessToken = AccessToken.newToken(this.email)
        this.token = accessToken.token
        this.tokenExpiresDate = accessToken.tokenExpiresDate
        this.salt = System.currentTimeMillis().toString()
        this.createdAt = now
        this.createdBy = this.createdBy ?: "system"
        this.updatedAt = now
        this.updatedBy = this.updatedBy ?: "system"
    }

    companion object {

        fun newUser(username: String, nickname: String? = null, description: String, imageCode: Int, email: String, password: String): User {
            return User(username, nickname, description, imageCode, email, password).apply {
                this.createdBy = username
                this.updatedBy = username
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', nickname=$nickname, description='$description', imageCode=$imageCode, email='$email', password='$password', salt='$salt', token='$token', tokenExpiresDate=$tokenExpiresDate, createdAt=$createdAt, createdBy=$createdBy, updatedAt=$updatedAt, updatedBy=$updatedBy)"
    }
}
