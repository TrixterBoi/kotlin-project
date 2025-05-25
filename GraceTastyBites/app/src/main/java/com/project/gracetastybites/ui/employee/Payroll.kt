package com.project.gracetastybites.ui.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class PayrollRecord(
    val payPeriod: String,
    val hoursWorked: Double,
    val totalPay: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PayrollScreen(menuDrawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val payrollRecords = remember {
        listOf(
            PayrollRecord("2024-05-01 to 2024-05-15", 80.0, 960.0),
            PayrollRecord("2024-05-16 to 2024-05-31", 75.5, 906.0),
            PayrollRecord("2024-06-01 to 2024-06-15", 82.0, 984.0)
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payroll") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { menuDrawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                    }
                }
            )
        }
    ) { padding ->
        if (payrollRecords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No payroll records found.")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(payrollRecords) { record ->
                    Card(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Pay Period: ${record.payPeriod}", style = MaterialTheme.typography.titleMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Hours Worked: ${record.hoursWorked}", style = MaterialTheme.typography.bodyMedium)
                            Spacer(Modifier.height(4.dp))
                            Text("Total Pay: £${"%.2f".format(record.totalPay)}", style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}