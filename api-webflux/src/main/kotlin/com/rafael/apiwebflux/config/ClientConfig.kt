package com.rafael.apiwebflux.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@ConfigurationProperties("network")
@Component
class ClientConfig {
    lateinit var clients: Map<String, ClientBaseConfig>
}

class ClientBaseConfig {
    lateinit var baseUrl: String
}
