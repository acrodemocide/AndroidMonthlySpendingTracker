package com.example.monthlyspendingtracker

//import androidx.appcompat.app.AppCompatActivity
//import androidx.compose.material.icons.Icons
//import androidx.compose.material3.icons.Icons
//import androidx.compose.material3.icons.Filled.Add
//import androidx.compose.material3.icons.Filled.KeyboardArrowDown
//import androidx.compose.material3.icons.Filled.Money
//import androidx.compose.material3.icons.Filled.Timeline
//import androidx.compose.material3.icons.filled.List
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.monthlyspendingtracker.ui.theme.MonthlySpendingTrackerTheme
import java.text.NumberFormat
import java.util.Currency


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
    var totalAmount by remember { mutableStateOf(0.0) }
    var purchaseAmount by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(Category.DANIEL_FUN) }
    val keyboardController = LocalSoftwareKeyboardController.current

    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("USD")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Display running total
        Text(
            text = "${format.format(totalAmount)}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Text box to enter purchase amount
        OutlinedTextField(
            value = purchaseAmount,
            onValueChange = { purchaseAmount = it },
            label = { Text("Enter purchase price (USD)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            leadingIcon = { Icon(painterResource(R.drawable.ic_baseline_attach_money_24), contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown menu for categories
        var expanded by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = selectedCategory.label,
            onValueChange = {},
            label = { Text("Category") },
            trailingIcon = {
                Icon(
                    painterResource(R.drawable.ic_baseline_keyboard_arrow_down_24),
                    contentDescription = null,
                    modifier = Modifier.clickable { expanded = true }
                )
            },
            readOnly = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { if (it.isFocused) expanded = true },
            colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black, unfocusedTextColor = Color.Black)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            for (category in Category.values()) {
                DropdownMenuItem(
                    text= { Text(category.label) },
                    onClick = {
                        selectedCategory = category
                        expanded = false
                    },
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Submit button
        IconButton(
            onClick = {
                val amount = purchaseAmount.toDoubleOrNull() ?: 0.0
                totalAmount += amount
                purchaseAmount = ""
                selectedCategory = Category.DANIEL_FUN
                keyboardController?.hide()
            }
        ) {
            Icon(painterResource(R.drawable.ic_baseline_add_24), contentDescription = null)
        }
    }
}