package com.example.mapper

import com.example.dto.CreateTaskDTO
import com.example.dto.TaskDTO
import com.example.entity.Task
import org.springframework.stereotype.Component

@Component
class TaskMapper {
    fun toDto(entity: Task): TaskDTO {
        return TaskDTO(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            completed = entity.completed
        );
    }

    fun toDto(tasks: List<Task>): List<TaskDTO> {
        return tasks.map { toDto(it) }
    }

    fun toEntity(dto: CreateTaskDTO): Task {
        return Task(
            title = dto.title,
            description = dto.description,
            completed = dto.completed
        )
    }
}