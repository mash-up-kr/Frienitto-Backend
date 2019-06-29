package org.frienitto.manitto.dto

import org.frienitto.manitto.dto.Participant
import java.time.LocalDate
import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
data class RoomResponse(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = 0,
        @get: NotBlank
        val title: String = "",
        @get: NotBlank
        val expires_date: LocalDate,
        @get: NotBlank
        val url: String? = "",
        @get: NotBlank
        val participant: List<Participant>
)
//
//"id": 1,
//"title": "망나니들 모임",
//"expires_date": "2020-01-01",
//"url": "",
//"participant": []