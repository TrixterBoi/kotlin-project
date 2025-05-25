package com.project.gracetastybites.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartModal(
    cart: List<String>,
    onCheckout: () -> Unit,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Cart", style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            if (cart.isEmpty()) {
                Text("Your cart is empty.", modifier = Modifier.padding(16.dp))
            } else {
                cart.groupingBy { it }.eachCount().forEach { (item, count) ->
                    Text("$item x$count", modifier = Modifier.padding(vertical = 8.dp))
                }
            }
            Spacer(Modifier.weight(1f))
            Button(
                onClick = onCheckout,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Checkout")
            }
        }
    }
}