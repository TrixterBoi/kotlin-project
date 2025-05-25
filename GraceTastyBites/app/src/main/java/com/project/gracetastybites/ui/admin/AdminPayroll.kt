package com.project.gracetastybites.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPayrollScreen(menuDrawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    // Example employee data (replace with real data source)
    var employees by remember {
        mutableStateOf(
            listOf(
                Employee(1, "Alice", "Smith", "07123456789", "alice@example.com", "Bob Smith: 07111222333", "Chef", 12.50),
                Employee(2, "John", "Doe", "07987654321", "john@example.com", "Jane Doe: 07999888777", "Waiter", 10.00)
            )
        )
    }
    var generalExpenses by remember { mutableStateOf("100.00") }

    val totalPayroll = employees.sumOf { it.pay }
    val generalExpensesValue = generalExpenses.toDoubleOrNull() ?: 0.0
    val totalExpenses = totalPayroll + generalExpensesValue

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Payroll") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { menuDrawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Employee Payroll", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(12.dp))
            LazyColumn(
                modifier = Modifier.weight(1f, fill = false)
            ) {
                items(employees) { emp ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("${emp.firstName} ${emp.lastName}", modifier = Modifier.weight(1f))
                        Text("£${"%.2f".format(emp.pay)} /hr", style = MaterialTheme.typography.bodyMedium)
                    }
                    Divider()
                }
            }
            Spacer(Modifier.height(16.dp))
            Text("Total Payroll: £${"%.2f".format(totalPayroll)}", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(24.dp))
            Text("General Expenses", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = generalExpenses,
                onValueChange = { generalExpenses = it },
                label = { Text("General Expenses (£)") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                isError = generalExpenses.toDoubleOrNull() == null
            )
            Spacer(Modifier.height(16.dp))
            Text("Total Expenses: £${"%.2f".format(totalExpenses)}", style = MaterialTheme.typography.titleLarge)
        }
    }
}