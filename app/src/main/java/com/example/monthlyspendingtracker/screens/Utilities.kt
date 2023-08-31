package com.example.monthlyspendingtracker.screens

import java.util.Calendar
import java.util.Date

fun getFirstOfCurrentMonth (): Date {
    val startOfCurrentMonth = Calendar.getInstance().apply {
        set(Calendar.DAY_OF_MONTH, 1) // Set to the first day of the month
    }.time
    return startOfCurrentMonth
}

fun getMonthFromNumber(dateNumber: Int?): String {
    val month = when (dateNumber) {
        0 -> "Jan"
        1 -> "Feb"
        2 -> "Mar"
        3 -> "Apr"
        4 -> "May"
        5 -> "Jun"
        6 -> "Jul"
        7 -> "Aug"
        8 -> "Sep"
        9 -> "Oct"
        10 -> "Nov"
        11 -> "Dec"
        else -> "Bad"
    }
    return month
}