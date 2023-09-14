package com.example.monthlyspendingtracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.*

@Entity(tableName = "expenses")
@TypeConverters(Converters::class)
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "date") val date: Date?,
    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "price") var price: Double,
    @ColumnInfo(name = "description") var description: String? = ""
)
