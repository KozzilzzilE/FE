package com.example.fe.feature.list.model

data class Concept(
    override val id: Long,
    override val title: String,
    override val difficulty: Difficulty,
    override val isCompleted: Boolean = false
) : DetailItem
