package org.frienitto.manitto.dto

data class MatchRequest(val roomId:Long, val participants: List<Long>)