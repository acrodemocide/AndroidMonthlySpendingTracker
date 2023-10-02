package com.example.monthlyspendingtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import com.example.monthlyspendingtracker.ui.theme.MonthlySpendingTrackerTheme

// TODO: this will be set in settings then loaded from DB
//val categories = listOf(
//    "Daniel Fun",
//    "Eating Out",
//    "Entertainment Activity",
//    "Gas",
//    "Groceries",
//    "Katie Fun",
//)

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