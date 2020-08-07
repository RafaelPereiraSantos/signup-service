package com.rafael.apiwebflux.http.exception

import com.rafael.common.models.ExceptionMessage
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.badRequest
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.io.PrintWriter
import java.io.StringWriter

@ControllerAdvice
class ControllerExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handle(ex: Exception): ResponseEntity<ExceptionMessage> {
        return badRequest()
            .body(ExceptionMessage(ex.createStackTrace(), "error.key"))
    }

    fun Throwable.createStackTrace(): String {
        val sw = StringWriter()
        this.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }
}
