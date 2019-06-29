package org.frienitto.manitto.config.propreties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource

@Configuration
@ConfigurationProperties(prefix = "frienitto.database")
@PropertySource(value = ["classpath:/db/frienitto-db-\${spring.profiles.active}.properties"])
class FrienittoDBProperties {

    lateinit var url: String
    lateinit var driverClassName: String
    lateinit var username: String
    lateinit var password: String
    lateinit var maxIdleConnection: String
    lateinit var minIdleConnection: String
}