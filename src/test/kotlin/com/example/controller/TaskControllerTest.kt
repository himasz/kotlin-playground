package com.example.controller

import com.example.dto.CreateTaskDTO
import com.example.dto.TaskDTO
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation::class)
class TaskManagerApplicationTests {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    @Order(1)
    fun getAllTasksReturnsEmptyList() {
        mockMvc.perform(get("/api/v1/tasks"))
            .andExpect(status().isOk)
            .andExpect(content().json("[]"))
    }

    @Test
    @Order(2)
    fun postTaskCreatesNewTask() {
        val task = CreateTaskDTO(title = "Test TaskDTO", description = "A sample task", completed = false)
        val taskJson = objectMapper.writeValueAsString(task)

        val response = mockMvc.perform(
            post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson)
        )
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        val createdTask = objectMapper.readValue(response, TaskDTO::class.java)
        // Check that an ID was generated
        assertTrue(createdTask.id > 0)
    }

    @Test
    @Order(3)
    fun getTaskByIdReturnsCorrectTask() {
        // First create a task
        val task = CreateTaskDTO(title = "Test TaskDTO 2", description = "Another task", completed = false)
        val taskJson = objectMapper.writeValueAsString(task)
        val postResponse = mockMvc.perform(
            post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson)
        )
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        val createdTask = objectMapper.readValue(postResponse, TaskDTO::class.java)
        // Then retrieve the task by id
        mockMvc.perform(get("/api/v1/tasks/${createdTask.id}"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Test TaskDTO 2"))
    }

    @Test
    @Order(4)
    fun putTaskByIdUpdatesTask() {
        // Create a task
        val task = CreateTaskDTO(title = "TaskDTO to update", description = "old description", completed = false)
        val taskJson = objectMapper.writeValueAsString(task)
        val postResponse = mockMvc.perform(
            post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson)
        )
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        val createdTask = objectMapper.readValue(postResponse, TaskDTO::class.java)

        // Update task details
        val updatedTask = createdTask.copy(
            title = "Updated TaskDTO",
            description = "new description",
            completed = true
        )
        val updatedJson = objectMapper.writeValueAsString(updatedTask)

        mockMvc.perform(
            put("/api/v1/tasks/${createdTask.id}")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.title").value("Updated TaskDTO"))
            .andExpect(jsonPath("$.completed").value(true))
    }

    @Test
    @Order(5)
    fun deleteTaskByIdRemovesTask() {
        // Create a task to delete
        val task = CreateTaskDTO(title = "TaskDTO to delete", description = "to be deleted", completed = false)
        val taskJson = objectMapper.writeValueAsString(task)
        val postResponse = mockMvc.perform(
            post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(taskJson)
        )
            .andExpect(status().isCreated)
            .andReturn().response.contentAsString

        val createdTask = objectMapper.readValue(postResponse, TaskDTO::class.java)

        // Delete the task
        mockMvc.perform(delete("/api/v1/tasks/${createdTask.id}"))
            .andExpect(status().isNoContent)

        // Confirm deletion by attempting to retrieve the task
        mockMvc.perform(get("/api/v1/tasks/${createdTask.id}"))
            .andExpect(status().isNotFound)
    }
}
