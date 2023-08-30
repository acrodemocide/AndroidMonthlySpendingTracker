package com.example.monthlyspendingtracker.screens

import java.util.Calendar
import java.util.Date

fun getFirstOfCurrentMonth (): Date {
    val startOfCurrentMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1) // Set to the first day of the month
    }.time
    return startOfCurrentMonth
}