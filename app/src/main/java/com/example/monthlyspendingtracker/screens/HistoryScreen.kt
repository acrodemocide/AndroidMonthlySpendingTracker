package com.example.monthlyspendingtracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.monthlyspendingtracker.R
import com.example.monthlyspendingtracker.data.AppDatabase
import com.example.monthlyspendingtracker.data.ExpenseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Currency
//import androidx.compose.material3.HorizontalDivider

@Composable
fun HistoryScreen () {
    val database = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java,
        "expenses-db"
    ).build()

    val currentMonth = getFirstOfCurrentMonth()

    var expenses: List<ExpenseEntity> by remember { mutableStateOf(emptyList<ExpenseEntity>()) }

    LaunchedEffect(Unit) {
        val dbExpenses = withContext(Dispatchers.IO) {
            database.expenseDao().getExpensesForMonth(currentMonth) ?: emptyList()
        }
        expenses = dbExpenses
    }

    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("USD")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            Text(
                text = "Transaction History",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
        }
        item {
            expenses.forEach { expense ->
                ListItem(
                    headlineContent = { Text("${format.format(expense.price)}") },
                    overlineContent = { Text("${expense.category}") },
                    supportingContent = { Text("${expense.date?.date} ${getMonthFromNumber(expense.date?.month)}") },
                    leadingContent = {
                        Icon(
                            painterResource(R.drawable.ic_baseline_attach_money_24),
                            contentDescription = "Localized description",
                        )
                    },
                    trailingContent = { Text("Transaction notes") },
                    colors = androidx.compose.material3.ListItemDefaults.colors(
                        containerColor = Color.White,
                        headlineColor = Color.Black,
                        overlineColor = Color.DarkGray,
                        supportingColor = Color.Gray,
                        trailingIconColor = Color.Gray
                    )
                )
            }
            //        HorizontalDivider()
        }
    }
}