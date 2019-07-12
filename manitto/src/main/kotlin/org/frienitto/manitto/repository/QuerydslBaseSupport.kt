package org.frienitto.manitto.repository

import org.frienitto.manitto.config.DatabaseConfig
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import kotlin.reflect.KClass

class QuerydslBaseSupport<T: Any>(clazz: KClass<T>) : QuerydslRepositorySupport(clazz.java) {

    @PersistenceContext(unitName = DatabaseConfig.PERSISTENCE_UNIT_NAME)
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }
}