package com.project.gracetastybites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.project.gracetastybites.ui.theme.GraceTastyBitesTheme
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.foundation.layout.padding

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraceTastyBitesTheme {
                MainScreen()
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

    val foodMenu = listOf("Login", "Staff Management", "Payroll", "Menu Management", "Employee Panel")
    var cart by remember { mutableStateOf(listOf<String>()) }

    // Left drawer: Menu
    ModalNavigationDrawer(
        drawerState = menuDrawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("Menu", style = MaterialTheme.typography.titleLarge, modifier = Modifier.padding(16.dp))
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

                // Main content
                Text(
                    "Welcome! Select items from the menu.",
                    modifier = Modifier.align(Alignment.Center)
                )

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


