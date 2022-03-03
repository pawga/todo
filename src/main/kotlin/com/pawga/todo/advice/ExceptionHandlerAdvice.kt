package com.pawga.todo.advice

import com.pawga.todo.data.models.ErrorResponse
import com.pawga.todo.exceptions.ApplicationException
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.server.ResponseStatusException

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
class ExceptionHandlerAdvice {

    private val log: Logger = LogManager.getLogger(ExceptionHandlerAdvice::class.java)

    @ExceptionHandler(
        ServletRequestBindingException::class,
        HttpMessageNotReadableException::class,
        MethodArgumentNotValidException::class,
        BindException::class
    )
    fun handleBadRequest(e: Exception?): ResponseEntity<Any> {
        log.error(HttpStatus.BAD_REQUEST.toString(), e)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleMethodNotAllowed(e: HttpRequestMethodNotSupportedException?): ResponseEntity<Any> {
        log.error(HttpStatus.METHOD_NOT_ALLOWED.toString(), e)
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build()
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException::class)
    fun handleNotAcceptable(e: HttpMediaTypeNotAcceptableException?): ResponseEntity<Any> {
        log.error(HttpStatus.NOT_ACCEPTABLE.toString(), e)
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build()
    }

    @ExceptionHandler(ApplicationException::class)
    fun handleApplicationException(e: ApplicationException): ResponseEntity<ErrorResponse> {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ErrorResponse(e.message ?: ""))
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(e: ResponseStatusException): ResponseEntity<ErrorResponse> {
        log.error(e.status.toString(), e)
        return ResponseEntity.status(e.status).body<ErrorResponse>(ErrorResponse(e.message))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handleAccessDeniedException(e: AccessDeniedException?): ResponseEntity<Any> {
        log.error(HttpStatus.FORBIDDEN.toString(), e)
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception?): ResponseEntity<Any> {
        log.error(HttpStatus.INTERNAL_SERVER_ERROR.toString(), e)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build()
    }
}
