package com.pawga.todo.data.repositories

import com.pawga.todo.data.models.TodoJpa
import org.springframework.data.jpa.repository.JpaRepository

interface TodoRepository : JpaRepository<TodoJpa, Long>
