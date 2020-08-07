package com.rafael.common.models

class HttpException(
    val statusCode: Int, val exceptionMessage: ExceptionMessage
): Exception(exceptionMessage.errorDescription)

class ExceptionMessage(val errorDescription: String, val errorKey: String)
