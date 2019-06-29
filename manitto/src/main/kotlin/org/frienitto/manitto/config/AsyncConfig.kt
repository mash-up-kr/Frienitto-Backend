package org.frienitto.manitto.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor

@EnableAsync
@Configuration
class AsyncConfig {

    @Bean
    fun threadPoolTaskExecutor(): ThreadPoolTaskExecutor {
        val pool = ThreadPoolTaskExecutor()
        pool.corePoolSize = 10
        pool.maxPoolSize = 10
        pool.setQueueCapacity(20)
        pool.setThreadNamePrefix("frienitto-task-")
        pool.initialize()

        return pool
    }
}