package com.example.sht.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sht.data.HabitDatabase
import com.example.sht.data.HabitEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = HabitDatabase.getInstance(application).habitDao()

    val habits: StateFlow<List<HabitEntity>> = dao.getAll()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun addHabit(title: String) {
        val trimmed = title.trim()
        if (trimmed.isEmpty()) return

        viewModelScope.launch {
            dao.insert(HabitEntity(title = trimmed, doneToday = false))
        }
    }

    fun toggleHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.update(habit.copy(doneToday = !habit.doneToday))
        }
    }

    fun deleteHabit(habit: HabitEntity) {
        viewModelScope.launch {
            dao.delete(habit)
        }
    }
}