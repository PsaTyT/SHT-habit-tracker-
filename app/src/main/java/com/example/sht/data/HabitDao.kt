package com.example.sht.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface HabitDao {

    @Query("""
        SELECT 
            h.id AS id,
            h.title AS title,
            CASE WHEN c.id IS NOT NULL THEN 1 ELSE 0 END AS doneToday
        FROM habits h
        LEFT JOIN completions c 
            ON c.habitId = h.id AND c.date = :today
        ORDER BY h.id ASC
    """)
    fun getAllWithStatus(today: LocalDate): Flow<List<HabitWithStatus>>

    @Insert
    suspend fun insert(habit: HabitEntity)

    @Update
    suspend fun update(habit: HabitEntity)

    @Delete
    suspend fun delete(habit: HabitEntity)
}