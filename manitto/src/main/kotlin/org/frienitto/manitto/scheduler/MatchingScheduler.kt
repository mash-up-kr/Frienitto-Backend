package org.frienitto.manitto.scheduler

import org.frienitto.manitto.domain.constant.RoomStatus
import org.frienitto.manitto.repository.RoomRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class MatchingScheduler(
        private val roomRepository: RoomRepository) {

    @Scheduled(cron = "5 0 * * * MON-SUN")
    fun makeExpiredmatchingList() {
        val list = roomRepository.findAll()
                .filter { it.status == RoomStatus.MATCHED }
                .filter {
                    val now = LocalDate.now()
                    now.isAfter(it.expiresDate)
                }
        list.forEach { it.expired() }
        roomRepository.saveAll(list)
    }
}