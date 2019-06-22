package org.frienitto.manitto.domain.constant

enum class MissionStatus(override var description: String) : EnumBase {
    DELIVERY("미션 전달"),
    ACCEPT("미션 수락"),
    REJECT("미션 거절"),
    COMPLETE("미션 완료");

    companion object {
        val POOL: Map<String, MissionStatus> = values()
                .map { it.description to it }
                .toMap()

        fun findBy(description: String): MissionStatus {
            return POOL[description] ?: throw IllegalArgumentException("Not found enum type")
        }
    }
}