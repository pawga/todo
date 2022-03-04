package com.pawga.todo.data.usecases

import com.pawga.todo.data.extensions.toJpa
import com.pawga.todo.data.extensions.toTodo
import com.pawga.todo.data.repositories.TodoRepository
import com.pawga.todo.domain.entities.Todo
import com.pawga.todo.domain.usecases.TodoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Repository

@Repository
class TodoServiceImpl(val todoRepository: TodoRepository) : TodoService {

    override fun findAll(pageable: Pageable): Page<Todo> = todoRepository.findAll(pageable).map { it.toTodo() }
    override fun findAll(): List<Todo> = todoRepository.findAll().map { it.toTodo() }
    override fun findById(id: Long): Todo? {
       val todoJpa = todoRepository.findById(id)
        return if (todoJpa.isEmpty) {
            null
        } else {
            todoJpa.get().toTodo()
        }
    }
    override fun save(station: Todo): Long = todoRepository.save(station.toJpa()).id
    override fun saveAll(stations: List<Todo>): List<Long> = todoRepository.saveAll(stations.map { it.toJpa() }).map { it.id }
    override fun update(station: Todo): Long = todoRepository.save(station.toJpa()).id
    override fun deleteAll() = todoRepository.deleteAll()
    override fun delete(station: Todo) = todoRepository.delete(station.toJpa())
}