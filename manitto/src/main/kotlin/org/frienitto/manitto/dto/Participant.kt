package org.frienitto.manitto.dto

import javax.validation.constraints.NotBlank

data class Participant(
        val id: Long? = 0,

        @get: NotBlank
        val userName: String = "",

        @get: NotBlank
        val image_code: Int?
)