package com.project.gracetastybites.ui.menu

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.launch

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
    val scope = rememberCoroutineScope()

    // Automatically expand the sheet when CartModal is shown
    LaunchedEffect(Unit) {
        scope.launch {
            sheetState.expand()
        }
    }

    val context = LocalContext.current
    var promoCode by remember { mutableStateOf("") }
    var promoApplied by remember { mutableStateOf(false) }
    var discount by remember { mutableStateOf(0.0) }
    var promoError by remember { mutableStateOf<String?>(null) }
    var deliveryOption by remember { mutableStateOf("Collection") }
    val deliveryFee = 2.50

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text("Your Cart", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(16.dp))
                if (cart.isEmpty()) {
                    Text("Your cart is empty.", style = MaterialTheme.typography.bodyMedium)
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 300.dp)
                    ) {
                        items(cart.keys.size) { idx ->
                            val itemName = cart.keys.elementAt(idx)
                            val count = cart[itemName] ?: 0
                            val detail = menuDetails[itemName]
                            if (detail != null) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(detail.name, style = MaterialTheme.typography.bodyLarge)
                                        Text("£${"%.2f".format(detail.price)} x $count", style = MaterialTheme.typography.bodySmall)
                                    }
                                    IconButton(
                                        onClick = { onUpdateCart(itemName, count - 1) },
                                        enabled = count > 1
                                    ) {
                                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                                    }
                                    Text("$count", modifier = Modifier.width(24.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                                    IconButton(
                                        onClick = { onUpdateCart(itemName, count + 1) }
                                    ) {
                                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                                    }
                                    IconButton(
                                        onClick = { onRemoveItem(itemName) }
                                    ) {
                                        Icon(Icons.Default.Clear, contentDescription = "Remove")
                                    }
                                }
                                Divider(modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
                Spacer(Modifier.height(16.dp))
                // Promo code input
                OutlinedTextField(
                    value = promoCode,
                    onValueChange = {
                        promoCode = it
                        promoError = null
                    },
                    label = { Text("Promo Code") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !promoApplied
                )
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (promoCode.equals("SAVE10", ignoreCase = true)) {
                            promoApplied = true
                            promoError = null
                            discount = 0.10
                        } else {
                            promoError = "Invalid promo code"
                            promoApplied = false
                            discount = 0.0
                        }
                    },
                    enabled = !promoApplied && promoCode.isNotBlank(),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(if (promoApplied) "Applied" else "Apply")
                }
                if (promoError != null) {
                    Text(promoError!!, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                }
                Spacer(Modifier.height(16.dp))
                // Collection/Delivery option
                Text("Choose Option:", style = MaterialTheme.typography.bodyMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(
                        selected = deliveryOption == "Collection",
                        onClick = { deliveryOption = "Collection" }
                    )
                    Text("Collection", modifier = Modifier.clickable { deliveryOption = "Collection" })
                    Spacer(Modifier.width(16.dp))
                    RadioButton(
                        selected = deliveryOption == "Delivery",
                        onClick = { deliveryOption = "Delivery" }
                    )
                    Text("Delivery (+£${"%.2f".format(deliveryFee)})", modifier = Modifier.clickable { deliveryOption = "Delivery" })
                }
                Spacer(Modifier.height(16.dp))
                // Totals
                val subtotal = cart.entries.sumOf { (item, count) ->
                    (menuDetails[item]?.price ?: 0.0) * count
                }
                val discountAmount = if (discount > 0.0) subtotal * discount else 0.0
                val deliveryAmount = if (deliveryOption == "Delivery" && cart.isNotEmpty()) deliveryFee else 0.0
                val total = subtotal - discountAmount + deliveryAmount

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Subtotal", style = MaterialTheme.typography.bodyMedium)
                    Text("£${"%.2f".format(subtotal)}", style = MaterialTheme.typography.bodyMedium)
                }
                if (discountAmount > 0.0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Discount", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                        Text("-£${"%.2f".format(discountAmount)}", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }
                if (deliveryAmount > 0.0) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Fee", style = MaterialTheme.typography.bodyMedium)
                        Text("£${"%.2f".format(deliveryAmount)}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
                Divider(modifier = Modifier.padding(vertical = 8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total", style = MaterialTheme.typography.titleMedium)
                    Text("£${"%.2f".format(total)}", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = {
                        Toast.makeText(context, "Order placed!", Toast.LENGTH_SHORT).show()
                        onCheckout()
                    },
                    enabled = cart.isNotEmpty(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Place Order")
                }
            }
        }
    }
}