package com.epam.quotesservice.controller

import com.epam.quotesservice.domain.exceptions.ExceptionEntity
import com.epam.quotesservice.domain.exceptions.QuoteValidateException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerAdvice {

    @ExceptionHandler(
        QuoteValidateException::class,
    )
    fun handleClientException(ex: RuntimeException): ResponseEntity<ExceptionEntity> =
        ResponseEntity(
            ExceptionEntity("Problem with request...", ex.localizedMessage),
            HttpStatus.BAD_REQUEST
        )

    @ExceptionHandler(Exception::class)
    fun handleException(ex: Exception): ResponseEntity<ExceptionEntity> =
        ResponseEntity(
            ExceptionEntity("Problem with service... ", ex.localizedMessage),
            HttpStatus.BAD_REQUEST
        )
}
