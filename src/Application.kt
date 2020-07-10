package com.rafael

import com.rafael.http.EndUserCompanies
import com.rafael.pubsub.EligibleConsumer

fun main(): Unit {

    val consumer = EligibleConsumer()
    consumer.up()

    val server = EndUserCompanies()
    server.start()

}




