package com.project.gracetastybites.ui.employee

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

data class Shift(
    val id: String,
    val date: String,
    val time: String,
    var status: ShiftStatus = ShiftStatus.PENDING
)

enum class ShiftStatus { PENDING, ACCEPTED, DECLINED }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShiftsScreen(menuDrawerState: DrawerState) {
    var shifts by remember {
        mutableStateOf(
            listOf(
                Shift("1", "2024-06-10", "09:00 - 17:00"),
                Shift("2", "2024-06-12", "13:00 - 21:00"),
                Shift("3", "2024-06-15", "08:00 - 16:00")
            )
        )
    }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Shifts") },
                navigationIcon = {
                    IconButton(onClick = { scope.launch { menuDrawerState.open() } }) {
                        Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier.fillMaxSize()
        ) {
            items(shifts) { shift ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("Date: ${shift.date}", style = MaterialTheme.typography.titleMedium)
                        Text("Time: ${shift.time}", style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            when (shift.status) {
                                ShiftStatus.PENDING -> {
                                    Button(
                                        onClick = {
                                            shifts = shifts.map {
                                                if (it.id == shift.id) it.copy(status = ShiftStatus.ACCEPTED) else it
                                            }
                                        }
                                    ) { Text("Accept") }
                                    Spacer(Modifier.width(8.dp))
                                    OutlinedButton(
                                        onClick = {
                                            shifts = shifts.map {
                                                if (it.id == shift.id) it.copy(status = ShiftStatus.DECLINED) else it
                                            }
                                        }
                                    ) { Text("Decline") }
                                }
                                ShiftStatus.ACCEPTED -> {
                                    Text("Accepted", color = MaterialTheme.colorScheme.primary)
                                }
                                ShiftStatus.DECLINED -> {
                                    Text("Declined", color = MaterialTheme.colorScheme.error)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}