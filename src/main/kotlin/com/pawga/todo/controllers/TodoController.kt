package com.pawga.todo.controllers

import com.pawga.todo.core.errors.ResourceDuplicateException
import com.pawga.todo.core.errors.ResourceNotFoundException
import com.pawga.todo.domain.entities.Todo
import com.pawga.todo.domain.usecases.TodoService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/v1/todos")
class TodoController(@Autowired private val todoService: TodoService) {

    var log: Logger = LoggerFactory.getLogger(TodoController::class.java)

    @Operation(
        operationId = "create",
        description = "Create a ToDo",
        responses = [ApiResponse(
            responseCode = "201",
            description = "ToDo",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
        )]
    )
    @PostMapping("/create")
    fun create(@RequestBody todo: Todo): ResponseEntity<*>? {
        log.debug("create")
        if (todoService.findById(todo.id) != null) {
            throw ResourceDuplicateException("Todo is duplicate for this id :: ${todo.id}")
        }
        val id = todoService.save(todo)
        val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(id).toUri()
        return ResponseEntity.created(location).build<Any>()
    }

    @Operation(
        operationId = "read",
        description = "Read ToDo by Id",
        responses = [ApiResponse(
            responseCode = "200",
            description = "id ToDo",
        )]
    )
    @GetMapping("/{id}")
    @Throws(ResourceNotFoundException::class)
    fun read(@PathVariable(value = "id") todoId: Long): ResponseEntity<Todo?> {
        log.debug("read")
        val todo = todoService.findById(todoId)
        if (todo == null) {
            throw ResourceNotFoundException("Todo not found for this id :: $todoId")
        } else {
            return  ResponseEntity.ok().body<Todo>(todo)
        }
    }

    @Operation(
        operationId = "update",
        description = "Update ToDo",
        responses = [ApiResponse(
            responseCode = "201",
            description = "ToDo",
            content = [Content(mediaType = MediaType.APPLICATION_JSON_VALUE)]
        )]
    )
    @PutMapping("/update")
    fun update(@RequestBody todo: Todo): ResponseEntity<*>? {
        log.debug("update")
        val find = todoService.findById(todo.id)
        if (find == null) {
            throw ResourceNotFoundException("Todo not found for this id :: ${todo.id}")
        } else {
            val id = todoService.update(todo)
            val location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(id).toUri()
            return ResponseEntity.created(location).build<Any>()
        }
    }

    @Operation(
        operationId = "delete",
        description = "Delete ToDo",
        responses = [ApiResponse(
            responseCode = "200",
            description = "Id",
        )]
    )
    @DeleteMapping("/{id}")
    fun delete(@PathVariable(value = "id") id: Long): ResponseEntity<*>? {
        log.debug("delete")
        val find = todoService.findById(id) ?: throw ResourceNotFoundException("Todo not found for this id :: $id")
        todoService.delete(find)
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

    @Operation(
        operationId = "delete all",
        description = "Delete all ToDo's",
        responses = [ApiResponse(
            responseCode = "200",
        )]
    )
    @DeleteMapping("/delete")
    fun deleteAll(): ResponseEntity<*>? {
        log.debug("deleteAll")
        todoService.deleteAll()
        return ResponseEntity<HttpStatus>(HttpStatus.OK)
    }

    @Operation(
        operationId = "all_by_page",
        description = "Get all todo's by page",
        responses = [ApiResponse(
            responseCode = "200",
            description = "Page",
        )]
    )
    @GetMapping("/all_by_page")
    fun getAllByPage(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "size", defaultValue = "5") size: Int,
    ): ResponseEntity<Page<Todo>> {
        log.debug("getAllByPage")
        val responsePage = todoService.findAll(PageRequest.of(page, size))
        return ResponseEntity.ok().body(responsePage)
    }

    @Operation(
        operationId = "all",
        description = "Get all todo's",
        responses = [ApiResponse(
            responseCode = "200",
            description = "Page",
        )]
    )
    @GetMapping("/all")
    fun getAll(): ResponseEntity<List<Todo>> {
        log.debug("getAll")
        val list = todoService.findAll()
        return ResponseEntity.ok().body(list)
    }
}