package com.example.monthlyspendingtracker.screens

import android.annotation.SuppressLint
import android.os.Environment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.monthlyspendingtracker.DeleteDialog
import com.example.monthlyspendingtracker.R
import com.example.monthlyspendingtracker.categories
import com.example.monthlyspendingtracker.data.AppDatabase
import com.example.monthlyspendingtracker.data.ExpenseEntity
import com.example.monthlyspendingtracker.data.GetDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.NumberFormat
import java.util.Currency


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen () {
    val database = GetDatabase()

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
    var selectedId = remember { mutableLongStateOf(0) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategory by remember { mutableStateOf(categories.first()) }
    val openDeleteConfirm = remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.White,
        modifier = Modifier.background(Color.White),
        content = { innerPadding ->
            DeleteDialog(
                isDialogOpen = openDeleteConfirm.value,
                onDialogClose = { openDeleteConfirm.value = false },
                onDialogConfirm = {
                    openDeleteConfirm.value = false
                    var selectedExpense = expenses.find { it.id == selectedId.value }
                    if (selectedExpense != null) {
                        CoroutineScope(Dispatchers.IO).launch {
                            database.expenseDao().deleteExpense(selectedExpense)
                            expenses = database.expenseDao().getExpensesForMonth(startOfCurrentMonth) ?: emptyList()
                        }
                    }
                })

            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
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
                                text = "Edit Transaction",
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

                            OutlinedTextField(
                                value = selectedCategory,
                                onValueChange = {selectedCategory = it},
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
                                for (category in categories) {
                                    DropdownMenuItem(
                                        text= { Text(category) },
                                        onClick = {
                                            selectedCategory = category
                                            expanded = false
                                        },
                                    )
                                }
                            }

                            Row {
                                TextButton(
                                    onClick = {
                                        openDialog.value = false
                                    },
                                    modifier = Modifier.width(110.dp)
                                ) {
                                    Text("Cancel")
                                }
                                TextButton(
                                    onClick = {
                                        openDialog.value = false
                                        var selectedExpense = expenses.find { it.id == selectedId.value }
                                        if (selectedExpense != null) {
                                            selectedExpense.price = purchaseAmount.drop(1).toDouble()
                                            selectedExpense.category = selectedCategory
                                            CoroutineScope(Dispatchers.IO).launch {
                                                database.expenseDao().updateExpense(selectedExpense)
                                            }
                                        }
                                    },
                                    modifier = Modifier.width(110.dp)
                                ) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            }

            val context = LocalContext.current
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                item {
                    Text(
                        text = "Transaction History",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp),
                        color = Color.Black
                    )
                    Button(
                        onClick = {
                            CoroutineScope(Dispatchers.IO).launch {
                                try {
                                    val expenses = database.expenseDao().getExpensesForMonth(startOfCurrentMonth) ?: emptyList()
                                    val csvString = expenses.joinToString("\n") {
                                        "${it.date?.date} ${getMonthFromNumber(it.date?.month)},${it.category},${it.price},${if (it.description != null) it.description else ""}"
                                    }
                                    val fileName = "expenses.csv"
                                    val filePath =
                                        Environment.getExternalStoragePublicDirectory(
                                            Environment.DIRECTORY_DOWNLOADS
                                        )
                                    if (!filePath.exists()) {
                                        filePath.mkdirs()
                                    }

                                    var file: File = File(filePath, fileName)

                                    file.createNewFile()
                                    val fileOutputStream =  FileOutputStream(file)
                                    val outputStream = OutputStreamWriter(fileOutputStream)
                                    outputStream.append(csvString)
                                    outputStream.close()
                                    fileOutputStream.flush()
                                    fileOutputStream.close()
                                    snackbarHostState.showSnackbar(
                                        "Save successful"
                                    )
                                } catch (e: Exception) {
                                    snackbarHostState.showSnackbar(
                                        "Save failed"
                                    )
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF0F9D58), contentColor = Color.White
                        ),
                    ) {
                        Text("Export CSV")
                    }
                }
                item {
                    expenses.forEach { expense ->
                        ListItem(
                            headlineContent = { Text("${format.format(expense.price)}") },
                            overlineContent = { Text("${expense.category}") },
                            supportingContent = { Text("${expense.date?.date} ${getMonthFromNumber(expense.date?.month)}") },
                            leadingContent = {
                                IconButton(
                                    onClick = {
                                        openDeleteConfirm.value = true
                                        selectedId.value = expense.id
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            trailingContent = { Text(
                                "${if (expense.description != null) expense.description else "No Description"}"
                            ) },
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
                                selectedId.value = expense.id
                                selectedCategory = expense.category
                            }
                        )
                    }
                }
            }
        }
    )
}