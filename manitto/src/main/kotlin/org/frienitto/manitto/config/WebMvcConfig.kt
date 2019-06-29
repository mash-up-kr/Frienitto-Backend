package org.frienitto.manitto.config

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class WebMvcConfig : WebMvcConfigurer {

    @Autowired
    lateinit var objectMapper: ObjectMapper

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(jackson2HttpMessageConverter())
    }

    private fun jackson2HttpMessageConverter(): MappingJackson2HttpMessageConverter {
        val converter = MappingJackson2HttpMessageConverter()
        val simpleModule = SimpleModule()

        simpleModule.addSerializer(LocalDateTime::class.java, LocalDateTimeJsonSerializer())
        simpleModule.addSerializer(LocalDate::class.java, LocalDateJsonSerializer())
        simpleModule.addDeserializer(LocalDate::class.java, LocalDateDeserializer())

        converter.objectMapper = objectMapper.registerModule(simpleModule)

        return converter
    }

    inner class LocalDateTimeJsonSerializer : JsonSerializer<LocalDateTime>() {

        @Throws(IOException::class)
        override fun serialize(localDateTime: LocalDateTime, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
            jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'hh:mm:ss").format(localDateTime))
        }

    }

    inner class LocalDateJsonSerializer : JsonSerializer<LocalDate>() {

        @Throws(IOException::class)
        override fun serialize(localDate: LocalDate, jsonGenerator: JsonGenerator, serializerProvider: SerializerProvider) {
            jsonGenerator.writeString(DateTimeFormatter.ofPattern("yyyy-MM-dd").format(localDate))
        }

    }

    inner class LocalDateDeserializer : JsonDeserializer<LocalDate>() {

        @Throws(IOException::class, JsonProcessingException::class)
        override fun deserialize(jsonParser: JsonParser, deserializationContext: DeserializationContext): LocalDate {
            return LocalDate.parse(jsonParser.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }
}