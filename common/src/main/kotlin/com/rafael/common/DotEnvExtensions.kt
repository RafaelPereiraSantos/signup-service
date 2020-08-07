package com.rafael.common

import io.github.cdimascio.dotenv.Dotenv

fun Dotenv.getOrThrow(env: String) = this[env]
    ?: throw IllegalStateException("Can't seem to find '$env' environment variable")