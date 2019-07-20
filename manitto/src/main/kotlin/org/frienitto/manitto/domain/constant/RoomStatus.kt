package org.frienitto.manitto.domain.constant

enum class RoomStatus(override var description: String) : EnumBase {

    CREATED("생성"),
    MATCHED("매칭 완료"),
    EXPIRED("종료");

    companion object {
        val POOL: Map<String, RoomStatus> = RoomStatus.values()
                .map { it.description to it }
                .toMap()

        fun findBy(description: String): RoomStatus {
            return POOL[description] ?: throw IllegalArgumentException("Not found enum type")
        }
    }
}