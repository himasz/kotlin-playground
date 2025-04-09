package com.example.controller

import com.example.dto.TaskDTO
import com.example.entity.Task
import com.example.service.TaskService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/tasks")
class TaskController(private val taskService: TaskService) {

    @GetMapping
    fun getAllTasks(): List<Task> = taskService.getAllTasks()

    @GetMapping("/{id}")
    fun getTaskById(@PathVariable id: Long): ResponseEntity<TaskDTO> {
        val task = taskService.getTaskById(id)
        return if (task != null) ResponseEntity.ok(task)
        else ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createTask(@RequestBody task: TaskDTO): ResponseEntity<Task> {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(task))
    }

    @PutMapping("/{id}")
    fun updateTask(@PathVariable id: Long, @RequestBody task: Task): ResponseEntity<Task> {
        val updated = taskService.updateTask(id, task)
        return if (updated != null) ResponseEntity.ok(updated)
        else ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteTask(@PathVariable id: Long): ResponseEntity<Void> {
        taskService.deleteTask(id)
        return ResponseEntity.noContent().build()
    }
}
