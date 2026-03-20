package com.example.fe.data

data class Application(
    override val id: Long,
    override val title: String,
    override val difficulty: Difficulty,
    override val isCompleted: Boolean = false
) : DetailItem
