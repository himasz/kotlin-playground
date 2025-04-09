package com.example.entity

import jakarta.persistence.*

@Entity
@Table(name = "tasks")
data class Task(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,  // Default value
    var title: String = "",  // Add default
    var description: String = "",  // Add default
    var completed: Boolean = false  // Default value
)
