package com.project.gracetastybites.ui.menu

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.project.gracetastybites.ui.login.LoginScreen
import com.project.gracetastybites.ui.login.ForgotPasswordScreen
import com.project.gracetastybites.ui.login.CreateAccountScreen
import com.project.gracetastybites.ui.partials.NavDrawer
import kotlinx.coroutines.launch

data class MenuItemDetail(
    val name: String,
    val description: String,
    val calories: Int,
    val price: Double
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(menuDrawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    var cart by remember { mutableStateOf<Map<String, Int>>(emptyMap()) }
    var showCart by remember { mutableStateOf(false) }
    val cartSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // For item detail modal
    var selectedItem by remember { mutableStateOf<MenuItemDetail?>(null) }
    var showItemDetail by remember { mutableStateOf(false) }
    val itemDetailSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    // Navigation state for auth screens
    var showLogin by remember { mutableStateOf(false) }
    var showForgotPassword by remember { mutableStateOf(false) }
    var showCreateAccount by remember { mutableStateOf(false) }

    // Drawer items with icons
    val drawerItems = listOf(
        Triple("Staff Panel", null, "Staff Panel"),
        Triple("Admin Panel", null, "Admin Panel")
    )

    // Categorized menu data
    val categorizedMenu = mapOf(
        "Starters" to listOf("Spring Rolls", "Garlic Bread"),
        "Mains" to listOf("Grilled Chicken", "Veggie Burger"),
        "Desserts" to listOf("Cheesecake", "Ice Cream"),
        "Drinks" to listOf("Cola", "Orange Juice")
    )

    // Details for each menu item
    val menuDetails = mapOf(
        "Spring Rolls" to MenuItemDetail("Spring Rolls", "Crispy rolls stuffed with veggies.", 180, 3.99),
        "Garlic Bread" to MenuItemDetail("Garlic Bread", "Toasted bread with garlic butter.", 220, 2.99),
        "Grilled Chicken" to MenuItemDetail("Grilled Chicken", "Juicy grilled chicken breast.", 350, 7.99),
        "Veggie Burger" to MenuItemDetail("Veggie Burger", "Burger with a plant-based patty.", 320, 6.99),
        "Cheesecake" to MenuItemDetail("Cheesecake", "Classic creamy cheesecake.", 400, 4.99),
        "Ice Cream" to MenuItemDetail("Ice Cream", "Two scoops of vanilla ice cream.", 250, 2.49),
        "Cola" to MenuItemDetail("Cola", "Chilled fizzy cola.", 150, 1.49),
        "Orange Juice" to MenuItemDetail("Orange Juice", "Freshly squeezed orange juice.", 120, 1.99)
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
                },
                drawerState = menuDrawerState
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
                        // Main content: Categorized food list with clickable items
                        Column(
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .fillMaxWidth()
                                .padding(vertical = 10.dp, horizontal = 24.dp)
                        ) {
                            categorizedMenu.forEach { (category, items) ->
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontSize = 22.sp,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                )
                                items.forEach { itemName ->
                                    val itemDetail = menuDetails[itemName]
                                    if (itemDetail != null) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    selectedItem = itemDetail
                                                    showItemDetail = true
                                                }
                                                .padding(vertical = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                itemDetail.name,
                                                style = MaterialTheme.typography.bodyLarge,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                "£${"%.2f".format(itemDetail.price)}",
                                                style = MaterialTheme.typography.bodyLarge,
                                                textAlign = TextAlign.End,
                                                modifier = Modifier.widthIn(min = 60.dp)
                                            )
                                        }
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
                            Text("Cart (${cart.values.sum()})")
                        }

                        // Cart modal
                        if (showCart) {
                            CartModal(
                                cart = cart,
                                menuDetails = menuDetails,
                                onUpdateCart = { item, newCount ->
                                    cart = cart.toMutableMap().apply {
                                        if (newCount > 0) put(item, newCount) else remove(item)
                                    }
                                },
                                onRemoveItem = { item ->
                                    cart = cart.toMutableMap().apply { remove(item) }
                                },
                                onCheckout = { /* Handle checkout */ },
                                onDismiss = { showCart = false },
                                sheetState = cartSheetState
                            )
                        }

                        // Item detail modal
                        if (showItemDetail && selectedItem != null) {
                            var quantity by remember { mutableStateOf(1) }
                            val item = selectedItem!!
                            val singlePrice = item.price
                            val totalPrice = singlePrice * quantity
                            val singleCalories = item.calories
                            val totalCalories = singleCalories * quantity
                            ModalBottomSheet(
                                onDismissRequest = { showItemDetail = false },
                                sheetState = itemDetailSheetState
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(item.name, style = MaterialTheme.typography.titleLarge)
                                    Spacer(Modifier.height(8.dp))
                                    Text(item.description, style = MaterialTheme.typography.bodyMedium)
                                    Spacer(Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Price (1): £${"%.2f".format(singlePrice)}")
                                        Text("Calories (1): $singleCalories")
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Button(
                                            onClick = { if (quantity > 1) quantity-- },
                                            enabled = quantity > 1,
                                            contentPadding = PaddingValues(0.dp)
                                        ) { Text("-") }
                                        Text(
                                            "$quantity",
                                            modifier = Modifier.padding(horizontal = 16.dp),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                        Button(
                                            onClick = { quantity++ },
                                            contentPadding = PaddingValues(0.dp)
                                        ) { Text("+") }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Divider()
                                    Spacer(Modifier.height(8.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text("Total Price: £${"%.2f".format(totalPrice)}", style = MaterialTheme.typography.titleMedium)
                                        Text("Total Calories: $totalCalories", style = MaterialTheme.typography.titleMedium)
                                    }
                                    Spacer(Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            cart = cart.toMutableMap().apply {
                                                put(item.name, (get(item.name) ?: 0) + quantity)
                                            }
                                            showItemDetail = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Add to Cart")
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