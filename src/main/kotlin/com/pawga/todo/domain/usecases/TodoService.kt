package com.pawga.todo.domain.usecases

import com.pawga.todo.domain.entities.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface TodoService {
    fun findAll(pageable: Pageable): Page<Todo>
    fun findAll(): List<Todo>
    fun findById(id: Long): Todo?
    fun save(station: Todo): Long
    fun saveAll(stations: List<Todo>): List<Long>
    fun update(station: Todo): Long
    fun deleteAll()
    fun delete(station: Todo)
}