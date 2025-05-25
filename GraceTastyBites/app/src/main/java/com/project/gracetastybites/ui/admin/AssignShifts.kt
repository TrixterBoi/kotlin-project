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
fun AssignShiftsScreen(menuDrawerState: DrawerState) {
    val scope = rememberCoroutineScope()
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
        }
    ) { padding ->
        // TODO: Implement assign shifts UI
        Text("Assign shifts to employees here.", modifier = Modifier.padding(padding))
    }
}