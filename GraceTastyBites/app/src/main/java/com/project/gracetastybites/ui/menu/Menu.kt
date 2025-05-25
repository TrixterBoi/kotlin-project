package com.project.gracetastybites.ui.menu

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import com.project.gracetastybites.ui.login.LoginScreen
import com.project.gracetastybites.ui.login.ForgotPasswordScreen
import com.project.gracetastybites.ui.login.CreateAccountScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen() {
    val menuDrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val foodMenu = listOf("Staff Management", "Payroll", "Menu Management", "Employee Panel")
    var cart by remember { mutableStateOf(listOf<String>()) }
    var showCart by remember { mutableStateOf(false) }
    val cartSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // Navigation state for auth screens
    var showLogin by remember { mutableStateOf(false) }
    var showForgotPassword by remember { mutableStateOf(false) }
    var showCreateAccount by remember { mutableStateOf(false) }

    // Drawer items with icons
    val drawerItems = listOf(
        Triple("Menu", Icons.Default.Home, "Menu"),
        Triple("Order History", Icons.Default.DateRange, "Order History"),
        Triple("Account Details", Icons.Default.Person, "Account Details"),
        Triple("Staff Panel", null, "Staff Panel"),
        Triple("Admin Panel", null, "Admin Panel")
    )

    ModalNavigationDrawer(
        drawerState = menuDrawerState,
        drawerContent = {
            NavDrawer(
                drawerItems = drawerItems,
                onItemClick = { /* Handle navigation here if needed */ },
                onLoginClick = {
                    showLogin = true
                    showForgotPassword = false
                    showCreateAccount = false
                    scope.launch { menuDrawerState.close() }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("GraceTastyBites") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { menuDrawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                when {
                    showLogin -> {
                        LoginScreen(
                            onLoginClick = { _, _ -> showLogin = false },
                            onForgotPasswordClick = {
                                showLogin = false
                                showForgotPassword = true
                            },
                            onCreateAccountClick = {
                                showLogin = false
                                showCreateAccount = true
                            }
                        )
                    }
                    showForgotPassword -> {
                        ForgotPasswordScreen(
                            onSubmit = { showForgotPassword = false }
                        )
                    }
                    showCreateAccount -> {
                        CreateAccountScreen(
                            onCreateAccountClick = { _, _, _, _ -> showCreateAccount = false },
                            onAlreadyCustomerClick = {
                                showCreateAccount = false
                                showLogin = true
                            }
                        )
                    }
                    else -> {
                        // Main content: Food list with Add buttons
                        Column(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .padding(24.dp)
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
                            onClick = { showCart = true },
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .padding(8.dp)
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                            Spacer(Modifier.width(8.dp))
                            Text("Cart (${cart.size})")
                        }

                        // Cart modal
                        if (showCart) {
                            val configuration = LocalConfiguration.current
                            val halfScreenHeight = (configuration.screenHeightDp.dp * 0.5f)
                            CartModal(
                                cart = cart,
                                onCheckout = { /* Handle checkout */ },
                                onDismiss = { showCart = false },
                                sheetState = cartSheetState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(halfScreenHeight)
                            )
                        }
                    }
                }
            }
        }
    }
}