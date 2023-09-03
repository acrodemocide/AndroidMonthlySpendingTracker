package com.example.monthlyspendingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.tooling.preview.Preview
import com.example.monthlyspendingtracker.ui.theme.MonthlySpendingTrackerTheme
import com.example.monthlyspendingtracker.screens.HomeScreen

//enum class Category(val label: String, val icon: Unit) {
//    DANIEL_FUN("Daniel Fun", Icons.Default.Money),
//    KATIE_FUN("Katie Fun", Icons.Default.Timeline),
//    DEBT("Debt", Icons.Default.List),
//    GROCERIES("Groceries", Icons.Default.KeyboardArrowDown)
//}

//enum class Category(val label: String) {
//    DANIEL_FUN("Daniel Fun"),
//    KATIE_FUN("Katie Fun"),
//    DEBT("Debt"),
//    GROCERIES("Groceries")
//}

// TODO: this will be set in settings then loaded from DB
val categories = listOf(
    "Daniel Fun",
    "Katie Fun",
    "Debt",
    "Groceries"
)

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

@ExperimentalComposeUiApi
@Composable
fun MyApp() {
    SpendingTrackerScaffold()
}