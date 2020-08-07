package com.rafael.apiwebflux.config

import com.rafael.apiwebflux.http.coEligibiltyRoute
import com.rafael.apiwebflux.http.reactorEligibiltyRoute
import com.rafael.apiwebflux.service.EligibleSearchService
import com.rafael.common.models.Eligible
import org.slf4j.LoggerFactory
import org.springdoc.core.annotations.RouterOperation
import org.springdoc.core.annotations.RouterOperations
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
    @RouterOperations(*[
        RouterOperation(
            path = "/reactor/eligibility", beanClass = EligibleSearchService::class, beanMethod = "reactorSearchBy", headers=["X-session"]
        ),
        RouterOperation(path = "/co/eligibility", beanClass = EligibleSearchService::class, beanMethod = "coSearchBy")
    ])
    fun rootRoute(eligibilitySearchService: EligibleSearchService) = coRouter {
        add(reactorEligibiltyRoute(eligibilitySearchService))
        add(coEligibiltyRoute(eligibilitySearchService))
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
