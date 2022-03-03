package com.pawga.todo.data.models

import io.swagger.v3.oas.annotations.media.Schema

class ErrorResponse(
    @field:Schema(
        description = "Error message",
        example = "Error processing the request",
        required = true
    ) val message: String
)