package com.project.gracetastybites.ui.admin

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEmployeesScreen(menuDrawerState: DrawerState) {
    val scope = rememberCoroutineScope()
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
        }
    ) { padding ->
        // TODO: Implement employee management UI
        Text("Manage employees here.", modifier = Modifier.padding(padding))
    }
}