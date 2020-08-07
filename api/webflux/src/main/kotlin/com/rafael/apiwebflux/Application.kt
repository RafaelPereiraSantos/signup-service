package com.rafael.apiwebflux

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springdoc.core.GroupedOpenApi
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.info.BuildProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableConfigurationProperties

class Application {
    @Bean
	fun customOpenAPI(): OpenAPI {
		return OpenAPI()
				.components(
                    Components().addSecuritySchemes("basicScheme",
						SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("basic"))
                )
				.info(
                    Info().title("Sample API").version("0.0.2")
						.license(License().name("Apache 2.0").url("http://springdoc.org"))
                )
	}

	@Bean
	fun employeesOpenApi(): GroupedOpenApi {
		val paths = arrayOf("/co/eligibility/**", "/reactor/eligibility/**", "/web-flux-controller/eligibility/**")
		return GroupedOpenApi.builder().group("eligibility").pathsToMatch(*paths).build()
	}
}

fun main() {
    runApplication<Application>()
}

