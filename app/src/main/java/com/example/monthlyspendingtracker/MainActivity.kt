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
import com.example.monthlyspendingtracker.screens.HomeScreen

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

@ExperimentalComposeUiApi
@Composable
fun MyApp() {
    SpendingTrackerScaffold{
        HomeScreen()
    }
}