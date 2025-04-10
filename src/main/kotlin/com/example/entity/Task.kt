package com.example.entity

import jakarta.persistence.*

@Entity
@Table(name = "tasks")
data class Task(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    var title: String = "",
    var description: String = "",
    var completed: Boolean = false
)
