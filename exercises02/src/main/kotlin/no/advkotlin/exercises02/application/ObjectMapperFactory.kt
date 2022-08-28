package no.advkotlin.exercises02.application

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule

object ObjectMapperFactory {

    fun create(): ObjectMapper {
        return configure(ObjectMapper())
    }

    fun configure(objectMapper: ObjectMapper): ObjectMapper {
        objectMapper.registerModule(Jdk8Module())
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerKotlinModule()
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        return objectMapper
    }

}
