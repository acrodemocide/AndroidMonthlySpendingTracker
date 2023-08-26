package com.example.monthlyspendingtracker.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.monthlyspendingtracker.Category
import com.example.monthlyspendingtracker.data.AppDatabase
import com.example.monthlyspendingtracker.data.ExpenseEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date

import com.example.monthlyspendingtracker.R

@ExperimentalComposeUiApi
@Composable
fun HomeScreen () {
    val database = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java,
        "expenses-db"
    ).build()

    // Initialize the running total with expenses from the current month
    val currentMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1) // Set to the first day of the month
    }.time

    var totalAmount by remember { mutableDoubleStateOf(0.0) }

    LaunchedEffect(Unit) {
        val amountFromDb = withContext(Dispatchers.IO) {
            database.expenseDao().getTotalAmountForMonth(currentMonth) ?: 0.0
        }
        totalAmount = amountFromDb
    }

    var purchaseAmount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.DANIEL_FUN) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("USD")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display running total
        Text(
            text = "${format.format(totalAmount)}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text box to enter purchase amount
        OutlinedTextField(
            value = purchaseAmount,
            onValueChange = { purchaseAmount = it },
            label = { Text("Enter purchase price (USD)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(painterResource(R.drawable.ic_baseline_attach_money_24), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown menu for categories
        var expanded by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = selectedCategory.label,
            onValueChange = {},
            label = { Text("Category") },
            trailingIcon = {
                Icon(
                    painterResource(R.drawable.ic_baseline_keyboard_arrow_down_24),
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (it.isFocused) expanded = true },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            for (category in Category.values()) {
                DropdownMenuItem(
                    text= { Text(category.label) },
                    onClick = {
                        selectedCategory = category
                        expanded = false
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit button
        IconButton(
            onClick = {
                val amount = purchaseAmount.toDoubleOrNull() ?: 0.0
                totalAmount += amount

                // Create an Expense object and insert it into the database
                val expense = ExpenseEntity(date = Date(), category = selectedCategory.label, price = amount)
                CoroutineScope(Dispatchers.IO).launch {
                    database.expenseDao().insertExpense(expense)
                }

                purchaseAmount = ""
                selectedCategory = Category.DANIEL_FUN
                keyboardController?.hide()
            }
        ) {
            Icon(painterResource(R.drawable.ic_baseline_add_24), contentDescription = null)
        }
    }
}