package org.frienitto.apigw

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.zuul.EnableZuulProxy

@EnableZuulProxy
@SpringBootApplication
class ApiGwApplication

fun main(args: Array<String>) {
    runApplication<ApiGwApplication>(*args)
}