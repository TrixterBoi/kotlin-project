package com.project.gracetastybites
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.gracetastybites.ui.theme.GraceTastyBitesTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraceTastyBitesTheme {
                MainScreen() // <-- Add this line to use your composable
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val menuDrawerState = rememberDrawerState(DrawerValue.Closed)
    val cartDrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val foodMenu = listOf("Burger", "Pizza", "Pasta", "Salad", "Sushi")
    var cart by remember { mutableStateOf(listOf<String>()) }

    // Left drawer: Navigation only
    ModalNavigationDrawer(
        drawerState = menuDrawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
                listOf("Login", "Staff Management", "Payroll", "Menu Management", "Employee Panel").forEach { item ->
                    Text(
                        item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    ) {
        // Right drawer: Cart
        ModalNavigationDrawer(
            drawerState = cartDrawerState,
            drawerContent = {
                ModalDrawerSheet(
                    modifier = Modifier.width(300.dp),
                    windowInsets = WindowInsets(0)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text("Cart", style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
                        IconButton(onClick = { scope.launch { cartDrawerState.close() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Close Cart")
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    if (cart.isEmpty()) {
                        Text("Your cart is empty.", modifier = Modifier.padding(16.dp))
                    } else {
                        cart.groupingBy { it }.eachCount().forEach { (item, count) ->
                            Text("$item x$count", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp))
                        }
                    }
                }
            },
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Hamburger menu at top left
                IconButton(
                    onClick = { scope.launch { menuDrawerState.open() } },
                    modifier = Modifier.align(Alignment.TopStart)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }

                // Main content: Food list with Add buttons
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                ) {
                    Text(
                        "Welcome! Select items from the food list.",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    foodMenu.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(item, modifier = Modifier.weight(1f))
                            Button(
                                onClick = { cart = cart + item },
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                            ) {
                                Text("Add")
                            }
                        }
                    }
                }

                // Cart button at bottom right
                Button(
                    onClick = { scope.launch { cartDrawerState.open() } },
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    Spacer(Modifier.width(8.dp))
                    Text("Cart (${cart.size})")
                }
            }
        }
    }
}