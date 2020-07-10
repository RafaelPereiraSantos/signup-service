package com.rafael

import com.rafael.http.EndUserCompanies
import com.rafael.pubsub.EligibleConsumer

fun main(args: Array<String>): Unit {

    val consumer = EligibleConsumer()
    consumer.up()

    val server = EndUserCompanies()
    server.start()

}




