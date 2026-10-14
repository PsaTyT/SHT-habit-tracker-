package com.example.sht.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sht.data.HabitDatabase
import com.example.sht.data.HabitEntity
import com.example.sht.data.HabitWithStatus
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val habitDao = HabitDatabase.getInstance(application).habitDao()
    private val completionDao = HabitDatabase.getInstance(application).completionDao()

    val habits: StateFlow<List<HabitWithStatus>> = habitDao
        .getAllWithStatus(LocalDate.now())
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addHabit(title: String) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return

        viewModelScope.launch {
            habitDao.insert(HabitEntity(title = trimmed))
        }
    }

    fun toggleHabit(habit: HabitWithStatus) {
        viewModelScope.launch {
            val today = LocalDate.now()
            if (habit.doneToday) {
                completionDao.deleteByHabitAndDate(habit.id, today)
            } else {
                completionDao.insert(
                    com.example.sht.data.CompletionEntity(habitId = habit.id, date = today)
                )
            }
        }
    }

    fun deleteHabit(habit: HabitWithStatus) {
        viewModelScope.launch {
            habitDao.delete(HabitEntity(id = habit.id, title = habit.title))
        }
    }
}