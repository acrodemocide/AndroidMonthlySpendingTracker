package com.example.monthlyspendingtracker.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import java.util.Date

@Dao
@TypeConverters(Converters::class)
interface ExpenseDao {
    @Insert
    fun insertExpense(expense: ExpenseEntity)

    @Query("SELECT SUM(price) FROM expenses WHERE date >= :startOfMonth")
    fun getTotalAmountForMonth(startOfMonth: Date): Double?

    @Query("SELECT * FROM expenses WHERE date >= :startOfMonth ORDER BY date DESC")
    fun getExpensesForMonth(startOfMonth: Date): List<ExpenseEntity>
}
