package com.example.monthlyspendingtracker

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalDrawer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
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
        }
    }
}

@Composable
fun SpendingTrackerScaffold(
    content: @Composable () -> Unit
) {
    val drawerState =
        androidx.compose.material.rememberDrawerState(androidx.compose.material.DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {Drawer()},
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
                content()
            }
        }
    )
}