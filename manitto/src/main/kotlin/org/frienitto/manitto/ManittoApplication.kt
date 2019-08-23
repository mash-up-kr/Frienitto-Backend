package org.frienitto.manitto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ManittoApplication

fun main(args: Array<String>) {
    runApplication<ManittoApplication>(*args)
}