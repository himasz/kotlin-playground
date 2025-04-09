package com.example.mapper

import com.example.dto.TaskDTO
import com.example.entity.Task
import org.springframework.stereotype.Component

@Component
class TaskMapper {
    fun toDto(entity: Task): TaskDTO {
        val dto = TaskDTO();
        dto.title = entity.title;
        dto.description = entity.description;
        dto.completed = entity.completed;
        return dto;
    }

    fun toEntity(dto: TaskDTO): Task {
        val entity = Task();
        entity.title = dto.title;
        entity.description = dto.description;
        entity.completed = dto.completed;
        return entity;
    }
}