package com.example.service

import com.example.dto.CreateTaskDTO
import com.example.dto.TaskDTO
import com.example.entity.Task
import com.example.mapper.TaskMapper
import org.springframework.stereotype.Service
import com.example.repository.TaskRepository

@Service
class TaskService(private val mapper: TaskMapper, private val taskRepository: TaskRepository) {

    fun getAllTasks(): List<TaskDTO> = mapper.toDto(taskRepository.findAll())

    fun getTaskById(id: Long): TaskDTO? {
        val task = taskRepository.findById(id)
        return when {
            task.isPresent -> mapper.toDto(task.get())
            else -> null
        }
    }

    fun createTask(taskDTO: CreateTaskDTO): Task = taskRepository.save(mapper.toEntity(taskDTO))

    fun updateTask(id: Long, updatedTask: CreateTaskDTO): TaskDTO? {
        val updatedOptional = taskRepository.findById(id).map {
            it.title = updatedTask.title
            it.description = updatedTask.description
            it.completed = updatedTask.completed
            taskRepository.save(it)
        }
        return when {
            updatedOptional.isPresent -> mapper.toDto(updatedOptional.get())
            else -> null
        }
    }

    fun deleteTask(id: Long) = taskRepository.deleteById(id)

}
