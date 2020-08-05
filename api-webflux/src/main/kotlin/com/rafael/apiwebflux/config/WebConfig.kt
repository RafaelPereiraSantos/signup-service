package com.rafael.apiwebflux.config

import com.rafael.apiwebflux.http.eligibiltyRoute
import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.models.Eligible
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer
import org.springframework.web.reactive.function.server.CoRouterFunctionDsl
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.coRouter
import java.io.IOException
import java.io.PrintWriter
import java.io.StringWriter

@Configuration
@EnableWebFlux
class WebConfig: WebFluxConfigurer {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Bean
    fun rootRoute(eligibilitySearchService: EligibleSearchService) = coRouter {
        add(eligibiltyRoute(eligibilitySearchService))

        configureErrorHandler()
    }

    fun CoRouterFunctionDsl.configureErrorHandler() {
        onError<IOException> { e, _ ->
            logger.error("${e.message} : Can't connect to the server", e)
            status(500).buildAndAwait()
        }
        onError<IllegalStateException> {  e, _ ->
            logger.warn("${e.message} : Can't connect to the server", e)
            status(500).buildAndAwait()
        }
        onError<Exception> { e, _ ->
            logger.warn(e.message, e)
            status(500).bodyValueAndAwait(Eligible(getStackTrace(e), "error key", "a", 1))
        }
    }

    fun getStackTrace(e: Throwable): String {
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }
}
