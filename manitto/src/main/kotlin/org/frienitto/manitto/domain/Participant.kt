package org.frienitto.manitto.domain

import javax.validation.constraints.NotBlank

data class Participant(

        val id: Long? = 0,

        @get: NotBlank
        val userName: String = "",

        @get: NotBlank
        val image_code: String? = ""
)

//{"id": 1, "username": "한태웅", "image_code": 1}