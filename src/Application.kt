package com.rafael

import com.rafael.http.Api
import com.rafael.pubsub.EligibleConsumer

fun main() {
    val consumer = EligibleConsumer()
    consumer.up()

    val server = Api()
    server.start()
}
