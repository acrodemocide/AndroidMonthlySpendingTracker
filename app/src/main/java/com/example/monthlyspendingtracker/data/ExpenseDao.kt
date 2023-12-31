package com.example.monthlyspendingtracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import java.util.Date

@Dao
@TypeConverters(Converters::class)
interface ExpenseDao {
    @Insert
    fun insertExpense(expense: ExpenseEntity)

    @Update
    fun updateExpense(expense: ExpenseEntity)

    @Delete
    fun deleteExpense(expense: ExpenseEntity)

    @Query("SELECT SUM(price) FROM expenses WHERE date >= :startOfMonth")
    fun getTotalAmountForMonth(startOfMonth: Date): Double?

    @Query("SELECT * FROM expenses WHERE date >= :startOfMonth ORDER BY date DESC")
    fun getExpensesForMonth(startOfMonth: Date): List<ExpenseEntity>
}
