package org.frienitto.manitto.config

import com.zaxxer.hikari.HikariDataSource
import org.frienitto.manitto.config.propreties.FrienittoDBProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*
import javax.persistence.EntityManagerFactory
import javax.sql.DataSource

@Configuration
@EnableJpaRepositories(basePackages = ["org.frienitto.manitto.repository"])
@EnableTransactionManagement
class DatabaseConfig {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(DatabaseConfig::class.java)
    }

    @Bean
    fun entityManagerFactory(dataSource: DataSource, jpaProperties: JpaProperties, @Value("spring.jpa.hibernate.ddl-auto") hibernateDdlAuto: String): LocalContainerEntityManagerFactoryBean {
        val emf = LocalContainerEntityManagerFactoryBean()
        emf.dataSource = dataSource
        emf.setPackagesToScan("org.frienitto.manitto.domain")

        val vendorAdapter = HibernateJpaVendorAdapter()
        vendorAdapter.setShowSql(jpaProperties.isShowSql)
        vendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl)

        emf.jpaVendorAdapter = vendorAdapter
        emf.setJpaProperties(additionalProperties(jpaProperties, hibernateDdlAuto))
        emf.afterPropertiesSet()

        return emf
    }

    @Bean
    fun transactionManager(entityManagerFactory: EntityManagerFactory): PlatformTransactionManager{
        return JpaTransactionManager(entityManagerFactory)
    }

    @Bean
    fun exceptionTranslator(): PersistenceExceptionTranslationPostProcessor {
        return PersistenceExceptionTranslationPostProcessor()
    }

    @Bean
    fun dataSource(dbProperties: FrienittoDBProperties): HikariDataSource {
        val dataSource = HikariDataSource()
        dataSource.jdbcUrl = dbProperties.url
        dataSource.username = dbProperties.username
        dataSource.password = dbProperties.password
        dataSource.maximumPoolSize = dbProperties.maxIdleConnection.toInt()
        dataSource.minimumIdle = dbProperties.minIdleConnection.toInt()

        return dataSource
    }

    private fun additionalProperties(jpaProperties: JpaProperties, hibernateDdlAuto: String): Properties {
        val properties = Properties()
        val physicalStrategy = SpringPhysicalNamingStrategy::class.java.toString().replace("class", "").trim()
        val implicitStrategy = SpringImplicitNamingStrategy::class.java.toString().replace("class", "").trim()
        val dialect = jpaProperties.databasePlatform
        val isShowSql = jpaProperties.isShowSql
        val isGenerateDdl = jpaProperties.isGenerateDdl

        logger.debug("--- jpa properties ---")
        logger.debug("hibernate ddl auto : $hibernateDdlAuto")
        logger.debug("hibernate dialect : $dialect")
        logger.debug("hibernate show sql : $isShowSql")
        logger.debug("hibernate generate ddl : $isGenerateDdl")
        logger.debug("hibernate physical naming strategy : $physicalStrategy")
        logger.debug("hibernate implicit naming strategy : $implicitStrategy")
        logger.debug("----------------------")
        properties.setProperty("hibernate.hbm2ddl-auto", hibernateDdlAuto)
        properties.setProperty("hibernate.dialect", dialect)
        properties.setProperty("hibernate.implicit_naming_strategy", implicitStrategy)
        properties.setProperty("hibernate.physical_naming_strategy", physicalStrategy)
        properties.setProperty("hibernate.connection.CharSet", "UTF-8")
        properties.setProperty("hibernate.connection.characterEncoding", "UTF-8")
        properties.setProperty("hibernate.connection.useUnicode", java.lang.Boolean.toString(true))

        return properties
    }
}