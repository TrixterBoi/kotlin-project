package com.project.gracetastybites.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
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

    // Favourites state
    var favourites by remember { mutableStateOf<Set<String>>(emptySet()) }

    // Drawer items with icons
    val drawerItems = listOf(
        Triple("Staff Panel", null, "Staff Management"),
        Triple("Admin Panel", null, "Admin Management"),
    )

    // Categorized menu data
    val categorizedMenu = mapOf(
        "Mains" to listOf("Grilled Chicken", "Veggie Burger"),
        "Desserts" to listOf("Cheesecake", "Ice Cream"),
        "Drinks" to listOf("Cola", "Orange Juice")
    )

    // Details for each menu item
    val menuDetails = mapOf(
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
                    title = { Text("Menu") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { menuDrawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Open Drawer")
                        }
                    },
                    actions = {
                        IconButton(onClick = { showCart = true }) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                if (showLogin) {
                    LoginScreen(
                        onLoginSuccess = { showLogin = false },
                        onForgotPasswordClick = {
                            showLogin = false
                            showForgotPassword = true
                        },
                        onCreateAccountClick = {
                            showLogin = false
                            showCreateAccount = true
                        }
                    )
                } else if (showForgotPassword) {
                    ForgotPasswordScreen(
                        onSubmit = { showForgotPassword = false }
                    )
                } else if (showCreateAccount) {
                    CreateAccountScreen(
                        onCreateAccountClick = { _, _, _, _ -> showCreateAccount = false },
                        onAlreadyCustomerClick = {
                            showCreateAccount = false
                            showLogin = true
                        }
                    )
                } else {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        categorizedMenu.forEach { (category, items) ->
                            Text(
                                text = category,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                            items.forEach { itemName ->
                                val item = menuDetails[itemName] ?: return@forEach
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            selectedItem = item
                                            showItemDetail = true
                                        }
                                        .padding(vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            item.name,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            item.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        "£${"%.2f".format(item.price)}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                        if (showItemDetail && selectedItem != null) {
                            var quantity by remember { mutableStateOf(1) }
                            val item = selectedItem!!
                            val singleCalories = item.calories
                            val totalCalories = singleCalories * quantity
                            ModalBottomSheet(
                                onDismissRequest = { showItemDetail = false },
                                sheetState = itemDetailSheetState
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = item.name,
                                            style = MaterialTheme.typography.titleLarge,
                                            modifier = Modifier.weight(1f)
                                        )
                                        IconButton(
                                            onClick = {
                                                favourites = if (favourites.contains(item.name)) {
                                                    favourites - item.name
                                                } else {
                                                    favourites + item.name
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = if (favourites.contains(item.name)) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                                contentDescription = if (favourites.contains(item.name)) "Unfavourite" else "Favourite"
                                            )
                                        }
                                    }
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        item.description,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Text(
                                        "Calories: $totalCalories kcal",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Spacer(Modifier.height(8.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("Quantity:", style = MaterialTheme.typography.bodyMedium)
                                        Spacer(Modifier.width(8.dp))
                                        IconButton(
                                            onClick = { if (quantity > 1) quantity-- },
                                            enabled = quantity > 1
                                        ) {
                                            Text("-", fontSize = 20.sp)
                                        }
                                        Text("$quantity", modifier = Modifier.width(32.dp), textAlign = TextAlign.Center)
                                        IconButton(
                                            onClick = { quantity++ }
                                        ) {
                                            Text("+", fontSize = 20.sp)
                                        }
                                    }
                                    Spacer(Modifier.height(16.dp))
                                    Button(
                                        onClick = {
                                            cart = cart.toMutableMap().apply {
                                                put(item.name, (cart[item.name] ?: 0) + quantity)
                                            }
                                            showItemDetail = false
                                        },
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text("Add to Cart - £${"%.2f".format(item.price * quantity)}")
                                    }
                                }
                            }
                        }
                        if (showCart) {
                            CartModal(
                                cart = cart,
                                menuDetails = menuDetails,
                                onUpdateCart = { item, count ->
                                    cart = cart.toMutableMap().apply {
                                        if (count > 0) put(item, count) else remove(item)
                                    }
                                },
                                onRemoveItem = { item ->
                                    cart = cart.toMutableMap().apply { remove(item) }
                                },
                                onCheckout = { showCart = false },
                                onDismiss = { showCart = false },
                                sheetState = cartSheetState
                            )
                        }
                    }
                }
            }
        }
    }
}