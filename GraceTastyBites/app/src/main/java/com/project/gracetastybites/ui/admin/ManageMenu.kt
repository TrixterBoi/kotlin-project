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

data class EditableMenuItem(
    val id: Int,
    var name: String,
    var description: String,
    var price: Double,
    var calories: Int,
    var available: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageMenuScreen(menuDrawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var menuItems by remember {
        mutableStateOf(
            listOf(
                EditableMenuItem(1, "Grilled Chicken", "Juicy grilled chicken breast.", 7.99, 350, true),
                EditableMenuItem(2, "Veggie Burger", "Plant-based patty burger.", 6.99, 320, true),
                EditableMenuItem(3, "Cheesecake", "Classic creamy cheesecake.", 4.99, 400, false)
            )
        )
    }
    var editingItem by remember { mutableStateOf<EditableMenuItem?>(null) }
    var creating by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Menu") },
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
                modifier = Modifier.padding(bottom = 80.dp) // Adjust as needed for your bottom bar
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Menu Item")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (menuItems.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No menu items found.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(menuItems) { item ->
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
                                    Text(item.name, style = MaterialTheme.typography.titleMedium)
                                    Text(item.description, style = MaterialTheme.typography.bodySmall)
                                    Spacer(Modifier.height(4.dp))
                                    Text("£${"%.2f".format(item.price)} | ${item.calories} kcal", style = MaterialTheme.typography.bodySmall)
                                    Text(
                                        if (item.available) "Available" else "Unavailable",
                                        color = if (item.available) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                                IconButton(onClick = { editingItem = item }) {
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
    if (editingItem != null) {
        EditMenuItemDialog(
            item = editingItem!!,
            onSave = { updated ->
                menuItems = menuItems.map { if (it.id == updated.id) updated else it }
                editingItem = null
            },
            onDelete = { id ->
                menuItems = menuItems.filterNot { it.id == id }
                editingItem = null
            },
            onDismiss = { editingItem = null }
        )
    }

    // Create dialog
    if (creating) {
        EditMenuItemDialog(
            item = EditableMenuItem(
                id = (menuItems.maxOfOrNull { it.id } ?: 0) + 1,
                name = "",
                description = "",
                price = 0.0,
                calories = 0,
                available = true
            ),
            onSave = { newItem ->
                menuItems = menuItems + newItem
                creating = false
            },
            onDelete = {}, // Not used for create
            onDismiss = { creating = false },
            isCreate = true
        )
    }
}

@Composable
fun EditMenuItemDialog(
    item: EditableMenuItem,
    onSave: (EditableMenuItem) -> Unit,
    onDelete: (Int) -> Unit,
    onDismiss: () -> Unit,
    isCreate: Boolean = false
) {
    var name by remember { mutableStateOf(item.name) }
    var description by remember { mutableStateOf(item.description) }
    var price by remember { mutableStateOf(item.price.toString()) }
    var calories by remember { mutableStateOf(item.calories.toString()) }
    var available by remember { mutableStateOf(item.available) }
    var error by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isCreate) "Add Menu Item" else "Edit Menu Item") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price (£)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = price.toDoubleOrNull() == null
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Calories") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = calories.toIntOrNull() == null
                )
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Available")
                    Spacer(Modifier.width(8.dp))
                    Switch(checked = available, onCheckedChange = { available = it })
                }
                if (error != null) {
                    Text(error!!, color = MaterialTheme.colorScheme.error)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                val p = price.toDoubleOrNull()
                val c = calories.toIntOrNull()
                if (name.isBlank() || description.isBlank() || p == null || c == null) {
                    error = "Please fill all fields with valid values."
                } else {
                    onSave(
                        item.copy(
                            name = name,
                            description = description,
                            price = p,
                            calories = c,
                            available = available
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
                    OutlinedButton(onClick = { onDelete(item.id) }) {
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