package dto

import jakarta.validation.constraints.NotBlank

data class TaskDTO(
    @field:NotBlank(message = "Title is required")
    var title: String = "",  // Add default
    var description: String = "",  // Add default
    var completed: Boolean = false  // Default value
)
