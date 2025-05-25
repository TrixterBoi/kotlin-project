package com.project.gracetastybites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.project.gracetastybites.ui.login.LoginScreen
import com.project.gracetastybites.ui.login.ForgotPasswordScreen
import com.project.gracetastybites.ui.login.CreateAccountScreen
import com.project.gracetastybites.ui.theme.GraceTastyBitesTheme
import kotlinx.coroutines.launch

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
            ModalDrawerSheet {
                Text(
                    "GraceTastyBites",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(16.dp)
                )
                drawerItems.forEach { (label, icon, contentDesc) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // Handle navigation for each item here
                            }
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (icon != null) {
                            Icon(
                                imageVector = icon,
                                contentDescription = contentDesc,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(Modifier.width(16.dp))
                        }
                        Text(
                            text = label,
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Start
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {
                        showLogin = true
                        showForgotPassword = false
                        showCreateAccount = false
                        scope.launch { menuDrawerState.close() }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Login")
                }
            }
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
                            onLoginClick = { email, password ->
                                // Handle login logic here
                                showLogin = false
                            },
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
                            onSubmit = { email ->
                                // Handle forgot password logic here
                                showForgotPassword = false
                            }
                        )
                    }
                    showCreateAccount -> {
                        CreateAccountScreen(
                            onCreateAccountClick = { firstName, secondName, email, password ->
                                // Handle account creation logic here
                                showCreateAccount = false
                            },
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

                        // Modal Bottom Sheet for Cart
                        if (showCart) {
                            val configuration = LocalConfiguration.current
                            val halfScreenHeight = (configuration.screenHeightDp.dp * 0.5f)
                            ModalBottomSheet(
                                onDismissRequest = { showCart = false },
                                sheetState = cartSheetState,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(halfScreenHeight)
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
                                        onClick = { /* Handle checkout */ },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 16.dp)
                                    ) {
                                        Text("Checkout")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}