package com.example.monthlyspendingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.monthlyspendingtracker.data.AppDatabase
import com.example.monthlyspendingtracker.data.ExpenseEntity
import com.example.monthlyspendingtracker.ui.theme.MonthlySpendingTrackerTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.NumberFormat
import java.util.Calendar
import java.util.Currency
import java.util.Date
import androidx.compose.material.ModalDrawer

//enum class Category(val label: String, val icon: Unit) {
//    DANIEL_FUN("Daniel Fun", Icons.Default.Money),
//    KATIE_FUN("Katie Fun", Icons.Default.Timeline),
//    DEBT("Debt", Icons.Default.List),
//    GROCERIES("Groceries", Icons.Default.KeyboardArrowDown)
//}

enum class Category(val label: String) {
    DANIEL_FUN("Daniel Fun"),
    KATIE_FUN("Katie Fun"),
    DEBT("Debt"),
    GROCERIES("Groceries")
}

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonthlySpendingTrackerTheme {
                MyApp()
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MonthlySpendingTrackerTheme {
        Greeting("Android")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        title = { Text(text = "Monthly Spending Tracker") },
        navigationIcon = {
            IconButton(onClick = onMenuClick) {
                Icon(painterResource(R.drawable.baseline_menu_24), contentDescription = null)
            }
        },
        // background color of topAppBar (cannot find "backgroundColor" -- not sure what to do here)
//        backgroundColor = Color(0xFF0F9D58)
    )
}

@Composable
fun Drawer() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        repeat(5) { item ->
            Text(text = "Item $item", color = Color.Black)
            Text(text = "Drawer")
        }
    }
}

@Composable
fun SpendingTrackerScaffold(
    topBar: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val drawerState =
        androidx.compose.material.rememberDrawerState(androidx.compose.material.DrawerValue.Closed)
//    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {Drawer()},
        content = {
            Column {
                topBar()
                content()
            }
        }
    )
}

@ExperimentalComposeUiApi
@Composable
fun MyApp() {
    val database = Room.databaseBuilder(
        LocalContext.current,
        AppDatabase::class.java,
        "expenses-db"
    ).build()

    // Initialize the running total with expenses from the current month
    val currentMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1) // Set to the first day of the month
    }.time

    var totalAmount by remember { mutableStateOf(0.0) }

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