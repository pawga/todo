package com.pawga.todo.data.extensions

import com.pawga.todo.data.models.TodoJpa
import com.pawga.todo.domain.entities.Todo

fun TodoJpa.toTodo() : Todo = Todo(id, title, detail, done)

fun Todo.toJpa() : TodoJpa = TodoJpa(id, title, detail, done)