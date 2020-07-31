package com.rafael.apiwebflux.exception

import org.springframework.boot.autoconfigure.web.ErrorProperties
import org.springframework.boot.autoconfigure.web.ResourceProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

//@Component
//@Order(-2)
//class GlobalErrorWebExceptionHandler(
//    errorAttributes: ErrorAttributes, resourceProperties: ResourceProperties
//) : AbstractErrorWebExceptionHandler(errorAttributes, resourceProperties) {
//
//    override protected fun getRoutingFunction(
//      errorAttributes: ErrorAttributes
//    ): RouterFunction<ServerResponse> {
//
//        return RouterFunctions.route(RequestPredicates.all()) { request ->
//            val errorPropertiesMap: Map<String, Object> = getErrorAttributes(request, false) as Map<String, Object>
//           ServerResponse.status(HttpStatus.BAD_REQUEST)
//             .contentType(MediaType.APPLICATION_JSON_UTF8)
//             .body(BodyInserters.fromObject(errorPropertiesMap))
//        }
//    }
//
//    private fun renderErrorResponse(
//       request: ServerRequest
//    ): Mono<ServerResponse> {
//
//       val errorPropertiesMap: Map<String, Object> = getErrorAttributes(request, false) as Map<String, Object>
//
//       return ServerResponse.status(HttpStatus.BAD_REQUEST)
//         .contentType(MediaType.APPLICATION_JSON_UTF8)
//         .body(BodyInserters.fromObject(errorPropertiesMap));
//    }
//}
