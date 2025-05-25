package com.project.gracetastybites.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartModal(
    cart: Map<String, Int>,
    menuDetails: Map<String, MenuItemDetail>,
    onUpdateCart: (String, Int) -> Unit,
    onRemoveItem: (String) -> Unit,
    onCheckout: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            val maxHeight = this.maxHeight
            val buttonHeightWithPadding = 56.dp + 32.dp

            val total = cart.entries.sumOf { (item, count) ->
                val price = menuDetails[item]?.price ?: 0.0
                price * count
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = buttonHeightWithPadding) // Ensures total is above the button
                    .align(Alignment.TopCenter)
            ) {
                Text("Cart", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                if (cart.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "Your cart is empty.",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    val headerHeight = 56.dp
                    val listMaxHeight = maxHeight - buttonHeightWithPadding - headerHeight

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = listMaxHeight)
                    ) {
                        cart.forEach { (item, count) ->
                            item {
                                val price = menuDetails[item]?.price ?: 0.0
                                val subtotal = price * count
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    IconButton(
                                        onClick = { onRemoveItem(item) },
                                        modifier = Modifier.size(32.dp)
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "Delete")
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(item)
                                        Text(
                                            "Price: £${"%.2f".format(price)}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        IconButton(
                                            onClick = { if (count > 1) onUpdateCart(item, count - 1) },
                                            enabled = count > 1,
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                                        }
                                        Text(
                                            "$count",
                                            modifier = Modifier.padding(horizontal = 8.dp),
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        IconButton(
                                            onClick = { onUpdateCart(item, count + 1) },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                                        }
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "£${"%.2f".format(subtotal)}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Divider()
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            "Total: £${"%.2f".format(total)}",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text("Checkout")
            }
        }
    }
}