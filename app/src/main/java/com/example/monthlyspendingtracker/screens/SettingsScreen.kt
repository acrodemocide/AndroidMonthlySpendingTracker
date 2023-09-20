package com.example.monthlyspendingtracker.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.monthlyspendingtracker.data.GetDatabase
import com.example.monthlyspendingtracker.data.SettingsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SettingsScreen () {
    val database = GetDatabase()

    var categories by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        try {
            val settingsCountFromDb = withContext(Dispatchers.IO) {
                database.settingsDao().getSettingsCount()
            }

            if (settingsCountFromDb == 0) {
                val settings = SettingsEntity(category = "Car,Clothes,Eating Out,Gas,Groceries,Vacation")
                withContext(Dispatchers.IO) {
                    database.settingsDao().insertSettings(settings)
                }
            }
        } catch (e: Exception) {
            println(e)
        }

        try {
            val settingsFromDb = withContext(Dispatchers.IO) {
                database.settingsDao().getSettings()
            }
            categories = settingsFromDb.first().category
        } catch (e: Exception) {
            println(e)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Enter categories separated by commas",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        val categoryArray = categories.split(",").toTypedArray()
        categoryArray.forEach { category ->
            ListItem(
                headlineContent = {
//                    Text("", modifier = Modifier.weight(1f))
                    Text(category)
                },
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(20.dp)
                    )
                }
            )}

        Button (
            onClick = { /*TODO*/ },
            colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0F9D58), contentColor = Color.White)
        ) {
            Text("Add Category")
        }
    }
}