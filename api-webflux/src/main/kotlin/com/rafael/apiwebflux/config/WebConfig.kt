package com.rafael.apiwebflux.config

import com.rafael.apiwebflux.http.route
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.config.EnableWebFlux
import org.springframework.web.reactive.config.WebFluxConfigurer

@Configuration
@EnableWebFlux
class WebConfig: WebFluxConfigurer {
    @Bean
    fun eligibleSearch() = route
}
