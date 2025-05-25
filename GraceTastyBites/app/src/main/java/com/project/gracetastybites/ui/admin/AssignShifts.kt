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

data class ShiftAssignment(
    val id: Int,
    var person: String,
    var date: String,
    var time: String,
    var status: String // "Accepted", "Pending", "Declined"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssignShiftsScreen(menuDrawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var assignments by remember {
        mutableStateOf(
            listOf(
                ShiftAssignment(1, "Alice Smith", "2024-06-10", "09:00 - 17:00", "Accepted"),
                ShiftAssignment(2, "John Doe", "2024-06-12", "13:00 - 21:00", "Pending")
            )
        )
    }
    var editingAssignment by remember { mutableStateOf<ShiftAssignment?>(null) }
    var creating by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Assign Shifts") },
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
                Icon(Icons.Default.Add, contentDescription = "Add Shift")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (assignments.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No shift assignments found.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(assignments) { assignment ->
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
                                    Text("Person: ${assignment.person}", style = MaterialTheme.typography.titleMedium)
                                    Text("Date: ${assignment.date}", style = MaterialTheme.typography.bodySmall)
                                    Text("Time: ${assignment.time}", style = MaterialTheme.typography.bodySmall)
                                    Text("Status: ${assignment.status}", style = MaterialTheme.typography.bodySmall)
                                }
                                IconButton(onClick = { editingAssignment = assignment }) {
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
    if (editingAssignment != null) {
        EditShiftAssignmentDialog(
            assignment = editingAssignment!!,
            onSave = { updated ->
                assignments = assignments.map { if (it.id == updated.id) updated else it }
                editingAssignment = null
            },
            onDelete = { id ->
                assignments = assignments.filterNot { it.id == id }
                editingAssignment = null
            },
            onDismiss = { editingAssignment = null }
        )
    }

    // Create dialog
    if (creating) {
        EditShiftAssignmentDialog(
            assignment = ShiftAssignment(
                id = (assignments.maxOfOrNull { it.id } ?: 0) + 1,
                person = "",
                date = "",
                time = "",
                status = "Pending"
            ),
            onSave = { newAssignment ->
                assignments = assignments + newAssignment
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
fun EditShiftAssignmentDialog(
    assignment: ShiftAssignment,
    onSave: (ShiftAssignment) -> Unit,
    onDelete: (Int) -> Unit,
    onDismiss: () -> Unit,
    isCreate: Boolean = false
) {
    var person by remember { mutableStateOf(assignment.person) }
    var date by remember { mutableStateOf(assignment.date) }
    var time by remember { mutableStateOf(assignment.time) }
    var status by remember { mutableStateOf(assignment.status) }
    var error by remember { mutableStateOf<String?>(null) }
    val statusOptions = listOf("Accepted", "Pending", "Declined")
    var expanded by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isCreate) "Add Shift Assignment" else "Edit Shift Assignment") },
        text = {
            Column {
                OutlinedTextField(
                    value = person,
                    onValueChange = { person = it },
                    label = { Text("Person") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = date,
                    onValueChange = { date = it },
                    label = { Text("Date (YYYY-MM-DD)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = time,
                    onValueChange = { time = it },
                    label = { Text("Time (e.g. 09:00 - 17:00)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = status,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Status") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        statusOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    status = option
                                    expanded = false
                                }
                            )
                        }
                    }
                }
                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                if (person.isBlank() || date.isBlank() || time.isBlank() || status.isBlank()) {
                    error = "Please fill all fields."
                } else {
                    onSave(
                        assignment.copy(
                            person = person,
                            date = date,
                            time = time,
                            status = status
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
                    OutlinedButton(onClick = { onDelete(assignment.id) }) {
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