package com.example.citypulseai.models

data class Message(
    val id: Long,
    val content: String,
    val isUser: Boolean,
    val timestamp: String,
    val isError: Boolean = false,
    val isSystem: Boolean = false
)