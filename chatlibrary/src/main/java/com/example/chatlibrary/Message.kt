package com.example.chatlibrary

data class Message(
    val text: String,
    val isSent: Boolean,
    val timestamp: Long = System.currentTimeMillis()
) 