package com.example.monthlyspendingtracker.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.room.Room
import com.example.monthlyspendingtracker.Category
import com.example.monthlyspendingtracker.data.AppDatabase
import com.example.monthlyspendingtracker.data.ExpenseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Currency
import java.util.Date

class HistoryScreenViewModel {
    @Composable
    public fun getExpensesForMonth(startOfMonth: Date): List<ExpenseEntity> {
        val database = Room.databaseBuilder(
            LocalContext.current,
            AppDatabase::class.java,
            "expenses-db"
        ).build()

        var expenses: List<ExpenseEntity> by remember { mutableStateOf(emptyList<ExpenseEntity>()) }

        LaunchedEffect(Unit) {
            val dbExpenses = withContext(Dispatchers.IO) {
                database.expenseDao().getExpensesForMonth(getFirstOfCurrentMonth()) ?: emptyList()
            }
            expenses = dbExpenses
        }

        return expenses
    }
}