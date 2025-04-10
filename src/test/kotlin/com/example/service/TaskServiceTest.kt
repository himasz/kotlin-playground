package com.example.service

import com.example.dto.CreateTaskDTO
import com.example.entity.Task
import com.example.mapper.TaskMapper
import com.example.repository.TaskRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.junit.jupiter.api.extension.ExtendWith
import java.util.*

@ExtendWith(MockitoExtension::class)
class TaskServiceTest {

    @Mock
    lateinit var taskRepository: TaskRepository

    val taskMapper = TaskMapper()

    lateinit var taskService: TaskService

    @BeforeEach
    fun setUp() {
        taskService = TaskService(taskMapper, taskRepository)
    }

    @Test
    fun `getAllTasks returns all tasks`() {
        val tasks = listOf(Task(id = 1, title = "One", description = "Desc"))
        `when`(taskRepository.findAll()).thenReturn(tasks)

        val result = taskService.getAllTasks()

        assertEquals(1, result.size)
        assertEquals("One", result[0].title)
    }

    @Test
    fun `getTaskById returns task if exists`() {
        val task = Task(id = 1, title = "FindMe", description = "Found")
        `when`(taskRepository.findById(1)).thenReturn(Optional.of(task))

        val result = taskService.getTaskById(1)

        assertEquals("FindMe", result?.title)
    }

    @Test
    fun `getTaskById throws exception if not found`() {
        `when`(taskRepository.findById(99)).thenReturn(Optional.empty())

        val taskById = taskService.getTaskById(99)
        assertNull(taskById)
    }

    @Test
    fun `createTask saves task`() {
        val input = CreateTaskDTO(title = "CreateMe", description = "New")
        val saved = Task(id = 1, title = input.title, description = input.description)

        `when`(taskRepository.save(taskMapper.toEntity(input))).thenReturn(saved)

        val result = taskService.createTask(input)

        assertEquals(1, result.id)
        assertEquals("CreateMe", result.title)
    }

    @Test
    fun `updateTask updates task fields`() {
        val existing = Task(id = 1, title = "Old", description = "Old desc", completed = false)
        val updated = CreateTaskDTO(title = "New", description = "New desc", completed = true)
        val saved =
            Task(id = 1, title = updated.title, description = updated.description, completed = updated.completed)

        `when`(taskRepository.findById(1)).thenReturn(Optional.of(existing))
        `when`(taskRepository.save(any(Task::class.java))).thenReturn(saved)

        val result = taskService.updateTask(1, updated)

        assertEquals("New", result?.title)
        assertTrue(result?.completed == true)
    }

    @Test
    fun `updateTask throws exception if not found`() {
        `when`(taskRepository.findById(42)).thenReturn(Optional.empty())

        val updateTask = taskService.updateTask(42, CreateTaskDTO(title = "Doesn't matter"))
        assertNull(updateTask)
    }

    @Test
    fun `deleteTask removes task if exists`() {
        taskService.deleteTask(1)
        verify(taskRepository).deleteById(1)
    }
}
