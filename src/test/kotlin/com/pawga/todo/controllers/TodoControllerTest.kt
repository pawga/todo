package com.pawga.todo.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.pawga.todo.domain.entities.Todo
import com.pawga.todo.domain.usecases.TodoService
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.mockito.BDDMockito.given
import org.mockito.internal.matchers.GreaterThan
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.*
import org.testcontainers.shaded.org.bouncycastle.asn1.cms.CMSAttributes.contentType

/**
 * Created by sivannikov on 06.03.2022
 */
@WebMvcTest(TodoController::class)
internal class TodoControllerTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    val base = "/v1/todos"

    @MockBean
    private lateinit var todoService: TodoService

    @Test
    fun create() {

        val mock = Todo(0, "title", "detail", false)
        val mockId = 1001L

        // given
        given(todoService.findById(mock.id)).willReturn(null)
        given(todoService.save(mock)).willReturn(mockId)


        // when
        mockMvc.post("$base/create") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mock)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                header { string(HttpHeaders.LOCATION, "http://localhost$base/create/$mockId") }
            }

    }

    @Test
    fun read() {
        val mock = Todo(0, "title", "detail", false)
        val mockId = 1001L

        // given
        given(todoService.findById(mockId)).willReturn(mock)

        // when
        val resultActions = mockMvc.get("$base/$mockId")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { GreaterThan(1) }
                jsonPath("id") { value(0) }
                jsonPath("title") { value("title") }
                jsonPath("detail") { value("detail") }
                jsonPath("done") { value(false) }
                // example for an array
                // jsonPath("$[0].id") { value(0) }
            }

        // second way
        val result = resultActions.andReturn()
        val contentAsString = result.response.contentAsString
        val todo = objectMapper.readValue<Todo>(contentAsString)
        assert(!todo.done)
    }

    @Test
    fun update() {
        val mock = Todo(1001L, "title", "detail", false)
        val mock2 = Todo(1002L, "title", "detail", false)
        val mockId = 1002L

        // given
        given(todoService.findById(mock.id)).willReturn(mock)
        given(todoService.findById(mock2.id)).willReturn(null)
        given(todoService.update(mock)).willReturn(mockId)

        // when
        mockMvc.put("$base/update") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mock2)
        }
            .andDo { print() }
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { GreaterThan(1) }
                jsonPath("message") { value("Todo not found for this id :: ${mock2.id}") }
            }

        mockMvc.put("$base/update") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(mock)
        }
            .andDo { print() }
            .andExpect {
                status { isCreated() }
                header { string(HttpHeaders.LOCATION, "http://localhost$base/update/$mockId") }
            }

    }

    @Test
    fun delete() {
        val mock = Todo(1001L, "title", "detail", false)
        val mockId = 1002L

        // given
        given(todoService.findById(mock.id)).willReturn(mock)
        given(todoService.findById(mockId)).willReturn(null)

        // when
        mockMvc.delete("$base/$mockId")
            .andDo { print() }
            .andExpect {
                status { isNotFound() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { GreaterThan(1) }
                jsonPath("message") { value("Todo not found for this id :: $mockId") }
            }

        mockMvc.delete("$base/${mock.id}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }

    }

    @Test
    fun deleteAll() {
        mockMvc.delete("$base/delete")
            .andDo { print() }
            .andExpect {
                status { isOk() }
            }
    }

    @Test
    fun getAll() {
        val list = listOf<Todo>(
            Todo(1001L, "title01", "detail01", false),
            Todo(1002L, "title02", "detail02", true))

        // given
        given(todoService.findAll()).willReturn(list)

        // when
        mockMvc.get("$base/all")
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$.length()") { GreaterThan(1) }
                jsonPath("$[0].id") { value(1001L) }
                jsonPath("$[0].title") { value("title01") }
                jsonPath("$[0].detail") { value("detail01") }
                jsonPath("$[0].done") { value(false) }
                jsonPath("$[1].id") { value(1002L) }
                jsonPath("$[1].title") { value("title02") }
                jsonPath("$[1].detail") { value("detail02") }
                jsonPath("$[1].done") { value(true) }
            }

    }
}