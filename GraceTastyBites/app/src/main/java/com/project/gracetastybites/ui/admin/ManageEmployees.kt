package com.project.gracetastybites.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class Employee(
    val id: Int,
    var firstName: String,
    var lastName: String,
    var phone: String,
    var email: String,
    var emergencyContact: String,
    var role: String,
    var pay: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEmployeesScreen(menuDrawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var employees by remember {
        mutableStateOf(
            listOf(
                Employee(1, "Alice", "Smith", "07123456789", "alice@example.com", "Bob Smith: 07111222333", "Chef", 12.50),
                Employee(2, "John", "Doe", "07987654321", "john@example.com", "Jane Doe: 07999888777", "Waiter", 10.00)
            )
        )
    }
    var editingEmployee by remember { mutableStateOf<Employee?>(null) }
    var creating by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Employees") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { menuDrawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { creating = true },
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Employee")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (employees.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No employees found.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(employees) { emp ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("${emp.firstName} ${emp.lastName}", style = MaterialTheme.typography.titleMedium)
                                    Text("Phone: ${emp.phone}", style = MaterialTheme.typography.bodySmall)
                                    Text("Email: ${emp.email}", style = MaterialTheme.typography.bodySmall)
                                    Text("Emergency: ${emp.emergencyContact}", style = MaterialTheme.typography.bodySmall)
                                    Text("Role: ${emp.role}", style = MaterialTheme.typography.bodySmall)
                                    Text("Pay: £${"%.2f".format(emp.pay)} /hr", style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = { editingEmployee = emp }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Edit dialog
    if (editingEmployee != null) {
        EditEmployeeDialog(
            employee = editingEmployee!!,
            onSave = { updated ->
                employees = employees.map { if (it.id == updated.id) updated else it }
                editingEmployee = null
            },
            onDelete = { id ->
                employees = employees.filterNot { it.id == id }
                editingEmployee = null
            },
            onDismiss = { editingEmployee = null }
        )
    }

    // Create dialog
    if (creating) {
        EditEmployeeDialog(
            employee = Employee(
                id = (employees.maxOfOrNull { it.id } ?: 0) + 1,
                firstName = "",
                lastName = "",
                phone = "",
                email = "",
                emergencyContact = "",
                role = "",
                pay = 0.0
            ),
            onSave = { newEmp ->
                employees = employees + newEmp
                creating = false
            },
            onDelete = {}, // Not used for create
            onDismiss = { creating = false },
            isCreate = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEmployeeDialog(
    employee: Employee,
    onSave: (Employee) -> Unit,
    onDelete: (Int) -> Unit,
    onDismiss: () -> Unit,
    isCreate: Boolean = false
) {
    var firstName by remember { mutableStateOf(employee.firstName) }
    var lastName by remember { mutableStateOf(employee.lastName) }
    var phone by remember { mutableStateOf(employee.phone) }
    var email by remember { mutableStateOf(employee.email) }
    var emergencyContact by remember { mutableStateOf(employee.emergencyContact) }
    var role by remember { mutableStateOf(employee.role) }
    var pay by remember { mutableStateOf(employee.pay.toString()) }
    var error by remember { mutableStateOf<String?>(null) }
    val roleOptions = listOf("Chef", "Waiter", "Manager", "Cleaner", "Cashier")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isCreate) "Add Employee" else "Edit Employee") },
        text = {
            Column {
                OutlinedTextField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    label = { Text("First Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    label = { Text("Last Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Phone Number") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email Address") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = emergencyContact,
                    onValueChange = { emergencyContact = it },
                    label = { Text("Emergency Contact") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Role") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        roleOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    role = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = pay,
                    onValueChange = { pay = it },
                    label = { Text("Pay (£/hr)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = pay.toDoubleOrNull() == null
                )
                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val payValue = pay.toDoubleOrNull()
                if (firstName.isBlank() || lastName.isBlank() || phone.isBlank() ||
                    email.isBlank() || emergencyContact.isBlank() || role.isBlank() || payValue == null
                ) {
                    error = "Please fill all fields with valid values."
                } else {
                    onSave(
                        employee.copy(
                            firstName = firstName,
                            lastName = lastName,
                            phone = phone,
                            email = email,
                            emergencyContact = emergencyContact,
                            role = role,
                            pay = payValue
                        )
                    )
                }
            }) {
                Text(if (isCreate) "Create" else "Save")
            }
        },
        dismissButton = {
            Row {
                if (!isCreate) {
                    OutlinedButton(onClick = { onDelete(employee.id) }) {
                        Text("Delete")
                    }
                    Spacer(Modifier.width(8.dp))
                }
                OutlinedButton(onClick = onDismiss) {
                    Text("Cancel")
                }
            }
        }
    )
}