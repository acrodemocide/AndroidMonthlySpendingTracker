package com.example.monthlyspendingtracker.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen () {
    val database = GetDatabase()

    var categories by remember { mutableStateOf("") }
    var settings: List<SettingsEntity> by remember { mutableStateOf(emptyList<SettingsEntity>()) }

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

            val settingsFromDb = withContext(Dispatchers.IO) {
                database.settingsDao().getSettings()
            }
            settings = settingsFromDb
            categories = settingsFromDb.first().category
        } catch (e: Exception) {
            println(e)
        }
    }

    val openDialog = remember { mutableStateOf(false) }
    var newCategory by remember { mutableStateOf("") }

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
                        text = "Add Category",
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        value = "",
                        onValueChange = { newCategory = it },
                        label = { Text("Enter new category") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
                    )

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
                                var selectedSetting = settings.first()
                                selectedSetting.category = selectedSetting.category + "," + newCategory
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.settingsDao().updateSettings(selectedSetting)
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
                modifier = Modifier.fillMaxWidth(),
                headlineContent = { Text(category)},
                leadingContent = {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(20.dp)
                    )
                }
            )}

        Button (
            onClick = { openDialog.value = true },
            colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0F9D58), contentColor = Color.White)
        ) {
            Text("Add Category")
        }
    }
}