package com.example.monthlyspendingtracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.monthlyspendingtracker.screens.HistoryScreen
import com.example.monthlyspendingtracker.screens.HomeScreen
import com.example.monthlyspendingtracker.screens.SettingsScreen
import kotlinx.coroutines.launch

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
        colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
            Color(0xFF0F9D58)
        )
    )
}

class DrawerItem(val name: String, val icon: ImageVector)

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SpendingTrackerScaffold() {
    val drawerState =
        androidx.compose.material3.rememberDrawerState(androidx.compose.material3.DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val items = listOf(DrawerItem("Home", Icons.Default.Home), DrawerItem("Edit History", Icons.Default.Edit), DrawerItem("Settings", Icons.Default.Settings))
    val selectedItem = remember { mutableStateOf(items[0]) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Spacer(Modifier.height(12.dp))
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item.icon, contentDescription = null) },
                        label = { Text(item.name) },
                        selected = item == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        content = {
            Column {
                TopBar(
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    }
                )
                if (selectedItem.value.name == "Home") {
                    HomeScreen()
                } else if (selectedItem.value.name == "Edit History") {
                    HistoryScreen()
                } else if (selectedItem.value.name == "Settings") {
                    SettingsScreen()
                } else {
                    HomeScreen()
                }
            }
        }
    )
}

@Composable
fun DeleteDialog(isDialogOpen: Boolean, onDialogClose: () -> Unit, onDialogConfirm: () -> Unit) {
    if (isDialogOpen) {
        AlertDialog(
            onDismissRequest = onDialogClose,
            title = { Text("Delete this expense?") },
            text = { Text("Are you sure you want to delete this expense?") },
            confirmButton = {
                Button(
                    onClick = {
                        onDialogConfirm()
                    },
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD93025)
                    )
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = onDialogClose,
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F9D58)
                    )
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}