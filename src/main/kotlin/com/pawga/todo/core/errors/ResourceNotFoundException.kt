package com.pawga.todo.core.errors

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(value = HttpStatus.NOT_FOUND)
class ResourceNotFoundException(message: String?) : Exception(message)

@ResponseStatus(value = HttpStatus.CONFLICT)
class ResourceDuplicateException(message: String?) : Exception(message)