package org.frienitto.manitto.repository

import com.querydsl.jpa.JPQLQuery
import org.frienitto.manitto.domain.QRoom
import org.frienitto.manitto.domain.QUser
import org.frienitto.manitto.domain.QUserRoomMap
import org.frienitto.manitto.domain.UserRoomMap

class UserRoomMapRepositoryImpl : UserRoomMapCustomRepository, QuerydslBaseSupport<UserRoomMap>(UserRoomMap::class) {

    companion object {
        private val qUser: QUser = QUser.user
        private val qRoom: QRoom = QRoom.room
        private val qUserRoomMap: QUserRoomMap = QUserRoomMap.userRoomMap
    }

    override fun findByRoomIdWithAllRelationship(roomId: Long): List<UserRoomMap> {
        val query: JPQLQuery<UserRoomMap> = from(qUserRoomMap)
                .innerJoin(qUserRoomMap.room, qRoom)
                .fetchJoin()
                .innerJoin(qUserRoomMap.user, qUser)
                .fetchJoin()
                .where(qUserRoomMap.room.id.eq(roomId))

        return query.fetch()
    }

    override fun findByUserIdWithAllRelationship(userId: Long): List<UserRoomMap> {
        val query: JPQLQuery<UserRoomMap> = from(qUserRoomMap)
                .innerJoin(qUserRoomMap.room, qRoom)
                .fetchJoin()
                .innerJoin(qUserRoomMap.user, qUser)
                .fetchJoin()
                .where(qUserRoomMap.user.id.eq(userId))

        return query.fetch()
    }

    override fun deleteByRoomId(roomId: Long): Long {
        return delete(qUserRoomMap)
                .where(qUserRoomMap.room.id.eq(roomId))
                .execute()
    }
}