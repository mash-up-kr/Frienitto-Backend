package org.frienitto.manitto

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ManittoApplication

fun main(args: Array<String>) {
    runApplication<ManittoApplication>(*args)
}