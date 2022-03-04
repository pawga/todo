package com.pawga.todo.domain.entities

data class Todo(
    val id: Long = 0,
    val title: String,
    val detail: String,
    val done: Boolean = false)
