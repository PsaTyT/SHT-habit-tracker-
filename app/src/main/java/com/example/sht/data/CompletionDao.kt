package com.example.sht.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface CompletionDao {

    @Query("SELECT * FROM completions WHERE habitId = :habitId ORDER BY date DESC")
    fun getByHabit(habitId: Long): Flow<List<CompletionEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM completions WHERE habitId = :habitId AND date = :date)")
    suspend fun isCompleted(habitId: Long, date: LocalDate): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(completion: CompletionEntity)

    @Query("DELETE FROM completions WHERE habitId = :habitId AND date = :date")
    suspend fun deleteByHabitAndDate(habitId: Long, date: LocalDate)

    @Query("DELETE FROM completions WHERE habitId = :habitId")
    suspend fun deleteByHabit(habitId: Long)
}