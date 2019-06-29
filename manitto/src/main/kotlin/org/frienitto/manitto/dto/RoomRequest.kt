package org.frienitto.manitto.dto

import java.time.LocalDate

data class RoomRequest(val name:String, val code:String, val expiresDate:LocalDate)