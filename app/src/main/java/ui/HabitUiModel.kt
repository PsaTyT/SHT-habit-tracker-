package com.example.sht.ui

data class HabitUiModel(
    val id: Long,
    val title: String,
    val doneToday: Boolean,
    val streak: Int
)