package com.project.gracetastybites.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import java.text.SimpleDateFormat
import java.util.*

data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Double
)

data class Order(
    val id: String,
    val date: Date,
    val items: List<OrderItem>
) {
    val total: Double get() = items.sumOf { it.price * it.quantity }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen() {
    // Example order data
    val exampleOrders = listOf(
        Order(
            id = "ORD123456",
            date = SimpleDateFormat("yyyy-MM-dd").parse("2024-06-01") ?: Date(),
            items = listOf(
                OrderItem("Spring Rolls", 2, 3.99),
                OrderItem("Grilled Chicken", 1, 7.99),
                OrderItem("Cola", 2, 1.49)
            )
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Order History") }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (exampleOrders.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No orders yet.", style = MaterialTheme.typography.bodyLarge)
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    items(exampleOrders) { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            elevation = CardDefaults.cardElevation(2.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    "Order #${order.id}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    "Date: ${SimpleDateFormat("dd MMM yyyy").format(order.date)}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Spacer(Modifier.height(8.dp))
                                order.items.forEach { item ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("${item.name} x${item.quantity}")
                                        Text("£${"%.2f".format(item.price * item.quantity)}")
                                    }
                                }
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Total", fontWeight = FontWeight.Bold)
                                    Text("£${"%.2f".format(order.total)}", fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}