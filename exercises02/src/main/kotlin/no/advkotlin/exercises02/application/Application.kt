package no.advkotlin.exercises02.application

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Component


@SpringBootApplication
@Configuration
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

@Component
@Primary
class ObjectMapperFactoryBean : ObjectMapper() {
	init {
		val log: Logger = LoggerFactory.getLogger(ObjectMapperFactoryBean::class.java)
		log.info("Configuring jackson object mapper")
		@Suppress("LeakingThis")
		ObjectMapperFactory.configure(this)
	}
}
