package com.example.dto

import jakarta.validation.constraints.NotBlank

data class TaskDTO(
    val id: Long = 0,
    @field:NotBlank(message = "Title is required")
    val title: String = "",
    val description: String = "",
    val completed: Boolean = false
)
