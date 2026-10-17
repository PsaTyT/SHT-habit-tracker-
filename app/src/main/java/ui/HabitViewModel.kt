package com.example.sht.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sht.data.CompletionEntity
import com.example.sht.data.HabitDatabase
import com.example.sht.data.HabitEntity
import com.example.sht.data.StreakCalculator
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class HabitViewModel(application: Application) : AndroidViewModel(application) {

    private val habitDao = HabitDatabase.getInstance(application).habitDao()
    private val completionDao = HabitDatabase.getInstance(application).completionDao()

    private val today: LocalDate = LocalDate.now()

    private val habitsWithStatus = habitDao.getAllWithStatus(today)
    private val allCompletions = completionDao.getAllCompletions()

    val habits: StateFlow<List<HabitUiModel>> =
        combine(habitsWithStatus, allCompletions) { habits, completions ->
            val completionsByHabit: Map<Long, List<CompletionEntity>> =
                completions.groupBy { it.habitId }

            habits.map { habit ->
                val dates = completionsByHabit[habit.id]
                    ?.map { it.date }
                    .orEmpty()

                    HabitUiModel(
                    id = habit.id,
                    title = habit.title,
                    doneToday = habit.doneToday,
                    streak = StreakCalculator.calculateStreak(dates, today)
                )
            }
        }.stateIn(
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

    fun toggleHabit(habit: HabitUiModel) {
        viewModelScope.launch {
            if (habit.doneToday) {
                completionDao.deleteByHabitAndDate(habit.id, today)
            } else {
                completionDao.insert(
                    CompletionEntity(habitId = habit.id, date = today)
                )
            }
        }
    }

    fun deleteHabit(habit: HabitUiModel) {
        viewModelScope.launch {
            habitDao.delete(HabitEntity(id = habit.id, title = habit.title))
        }
    }
}