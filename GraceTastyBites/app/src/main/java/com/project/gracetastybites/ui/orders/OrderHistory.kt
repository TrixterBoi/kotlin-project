package com.project.gracetastybites.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

data class Order(
    val id: Int,
    val date: String,
    val items: List<String>,
    val total: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen() {
    // Sample data
    val orders = remember {
        listOf(
            Order(1, "2024-06-01", listOf("Spring Rolls", "Veggie Burger"), 10.98),
            Order(2, "2024-05-28", listOf("Grilled Chicken", "Cola"), 9.48),
            Order(3, "2024-05-20", listOf("Cheesecake", "Orange Juice"), 6.98)
        )
    }
    var helpOrderId by remember { mutableStateOf<Int?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order History") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (orders.isEmpty()) {
                Text(
                    "No orders yet.",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.titleMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(orders.size) { idx ->
                        val order = orders[idx]
                        OrderCard(
                            order = order,
                            onReorder = { /* handle reorder */ },
                            onGetHelp = { helpOrderId = order.id }
                        )
                    }
                }
            }

            // Simple help dialog
            if (helpOrderId != null) {
                AlertDialog(
                    onDismissRequest = { helpOrderId = null },
                    title = { Text("Get Help") },
                    text = { Text("How can we help you with order #${helpOrderId}?") },
                    confirmButton = {
                        Button(onClick = { helpOrderId = null }) { Text("OK") }
                    }
                )
            }
        }
    }
}

@Composable
fun OrderCard(
    order: Order,
    onReorder: () -> Unit,
    onGetHelp: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Order #${order.id}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onGetHelp) {
                        Icon(Icons.Default.Info, contentDescription = "Get Help")
                    }
                }
                Text(
                    "Date: ${order.date}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Items: ${order.items.joinToString()}",
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Total: £${"%.2f".format(order.total)}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(12.dp))
                Button(
                    onClick = onReorder,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Re-order")
                    Spacer(Modifier.width(8.dp))
                    Text("Re-order")
                }
            }
        }
    }
}