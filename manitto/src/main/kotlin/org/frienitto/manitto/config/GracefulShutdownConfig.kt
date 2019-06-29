package org.frienitto.manitto.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextClosedEvent
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import org.springframework.stereotype.Component

@Component
class GracefulShutdownConfig : ApplicationListener<ContextClosedEvent> {

    @Autowired
    lateinit var context: ApplicationContext

    override fun onApplicationEvent(event: ContextClosedEvent) {
        val threadPool = context.getBean("threadPoolTaskExecutor") as ThreadPoolTaskExecutor
        threadPool.setAwaitTerminationSeconds(30)
    }
}