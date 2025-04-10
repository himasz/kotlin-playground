package com.example.taskmanager

import com.example.dto.CreateTaskDTO
import com.example.dto.TaskDTO
import com.example.repository.TaskRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TaskControllerTestIT {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var taskRepository: TaskRepository

    @LocalServerPort
    var port: Int = 0

    private fun baseUrl() = "http://localhost:$port/api/v1/tasks"

    @BeforeEach
    fun cleanUp() {
        taskRepository.deleteAll()
    }

    @Test
    fun `create, get, update and delete a task`() {
        // CREATE
        val createTaskDTO = CreateTaskDTO(title = "Integration Task", description = "Integration Desc")
        val createResponse = restTemplate
            .postForEntity(
                baseUrl(),
                createTaskDTO, TaskDTO::class.java
            )
        assertEquals(HttpStatus.CREATED, createResponse.statusCode)
        val createdTask = createResponse.body!!
        assertNotNull(createdTask.id)
        assertEquals(createTaskDTO.title, createdTask.title)

        // GET ALL
        val getAll = restTemplate.getForEntity(baseUrl(), Array<TaskDTO>::class.java)
        assertEquals(HttpStatus.OK, getAll.statusCode)
        assertEquals(1, getAll.body!!.size)

        // GET BY ID
        val getById = restTemplate.getForEntity("${baseUrl()}/${createdTask.id}", TaskDTO::class.java)
        assertEquals(HttpStatus.OK, getById.statusCode)
        assertEquals(createdTask.id, getById.body!!.id)

        // UPDATE
        val updatedDTO = CreateTaskDTO(title = "Updated Title", description = "Updated Desc", completed = true)
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val request = HttpEntity(updatedDTO, headers)
        val updateResponse = restTemplate.exchange(
            "${baseUrl()}/${createdTask.id}",
            HttpMethod.PUT,
            request,
            TaskDTO::class.java
        )
        assertEquals(HttpStatus.OK, updateResponse.statusCode)
        assertEquals("Updated Title", updateResponse.body!!.title)
        assertTrue(updateResponse.body!!.completed)

        // DELETE
        val deleteResponse = restTemplate.exchange(
            "${baseUrl()}/${createdTask.id}",
            HttpMethod.DELETE,
            null,
            Void::class.java
        )
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.statusCode)

        // GET BY ID AFTER DELETE
        val getAfterDelete = restTemplate.getForEntity("${baseUrl()}/${createdTask.id}", String::class.java)
        assertEquals(HttpStatus.NOT_FOUND, getAfterDelete.statusCode)
    }
}
