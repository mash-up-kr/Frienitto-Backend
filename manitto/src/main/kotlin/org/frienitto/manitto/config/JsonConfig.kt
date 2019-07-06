package org.frienitto.manitto.config

import com.fasterxml.jackson.annotation.JsonInclude
import org.springframework.context.annotation.Configuration
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary


@Configuration
class JsonConfig {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper {
        return Jackson2ObjectMapperBuilder.json()
                .propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .build()
    }
}