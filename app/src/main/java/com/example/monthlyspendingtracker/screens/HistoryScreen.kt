package com.example.monthlyspendingtracker.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen () {
    val database = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java,
        "expenses-db"
    ).build()

    val startOfCurrentMonth = getFirstOfCurrentMonth()

    var expenses: List<ExpenseEntity> by remember { mutableStateOf(emptyList<ExpenseEntity>()) }

    LaunchedEffect(Unit) {
        val dbExpenses = withContext(Dispatchers.IO) {
            database.expenseDao().getExpensesForMonth(startOfCurrentMonth) ?: emptyList()
        }
        expenses = dbExpenses
    }

    var purchaseAmount by remember { mutableStateOf("") }
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("USD")

    val openDialog = remember { mutableStateOf(false) }
//    var selectedAmount = remember { mutableDoubleStateOf(0.0) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "This area typically contains the supportive text " +
                                "which presents the details regarding the Dialog's purpose.",
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    // Text box to edit purchase amount
                    OutlinedTextField(
                        value = purchaseAmount,
                        onValueChange = { purchaseAmount = it },
                        label = { Text("Edit purchase price (USD)") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        leadingIcon = { Icon(painterResource(R.drawable.ic_baseline_attach_money_24), contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
                    )
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            openDialog.value = false
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    }

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
                    ),
                    modifier = Modifier.clickable {
                        openDialog.value = true
                        purchaseAmount = "${format.format(expense.price)}"
                    }
                )
            }
            //        HorizontalDivider()
        }
    }
}