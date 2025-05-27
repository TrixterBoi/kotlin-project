package com.project.gracetastybites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.project.gracetastybites.data.SessionManager
import com.project.gracetastybites.data.UserRole
import com.project.gracetastybites.ui.menu.MenuScreen
import com.project.gracetastybites.ui.orders.OrderHistoryScreen
import com.project.gracetastybites.ui.account.AccountDetailsScreen
import com.project.gracetastybites.ui.login.LoginScreen
import com.project.gracetastybites.ui.employee.ShiftsScreen
import com.project.gracetastybites.ui.employee.PayrollScreen
import com.project.gracetastybites.ui.employee.EmployeeInfoScreen
import com.project.gracetastybites.ui.partials.NavDrawer
import com.project.gracetastybites.ui.theme.GraceTastyBitesTheme
import com.project.gracetastybites.ui.admin.AssignShiftsScreen
import com.project.gracetastybites.ui.admin.AdminPayrollScreen
import com.project.gracetastybites.ui.admin.ManageMenuScreen
import com.project.gracetastybites.ui.admin.ManageEmployeesScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraceTastyBitesTheme {
                // Session state
                val sessionState = remember { mutableStateOf(SessionManager.userRole) }
                DisposableEffect(Unit) {
                    val listener = { sessionState.value = SessionManager.userRole }
                    SessionManager.addListener(listener)
                    onDispose { SessionManager.removeListener(listener) }
                }

                var isEmployeePanel by remember { mutableStateOf(false) }
                var isAdminPanel by remember { mutableStateOf(false) }
                var selectedTab by remember { mutableStateOf(0) }
                var employeeTab by remember { mutableStateOf(0) }
                var adminTab by remember { mutableStateOf(0) }
                val menuDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var shouldCloseDrawer by remember { mutableStateOf(false) }
                var showLoginScreen by remember { mutableStateOf(false) }

                // Drawer items based on role
                val drawerItems = when {
                    isEmployeePanel -> listOf(Triple("Return Home", Icons.Default.Home, "Return Home"))
                    isAdminPanel -> listOf(Triple("Return Home", Icons.Default.Home, "Return Home"))
                    else -> {
                        val items = mutableListOf<Triple<String, Any?, String>>()
                        if (sessionState.value == UserRole.STAFF || sessionState.value == UserRole.ADMIN)
                            items.add(Triple("Employee Panel", Icons.Default.Face, "Employee Management"))
                        if (sessionState.value == UserRole.ADMIN)
                            items.add(Triple("Admin Panel", Icons.Default.Info, "Admin Management"))
                        items
                    }
                }

                // Close drawer after navigation
                LaunchedEffect(shouldCloseDrawer) {
                    if (shouldCloseDrawer) {
                        menuDrawerState.close()
                        shouldCloseDrawer = false
                    }
                }

                    Scaffold(
                        bottomBar = {
                            if (!menuDrawerState.isOpen) {
                                when {
                                    isEmployeePanel -> EmployeePanelBottomBar(employeeTab) { employeeTab = it }
                                    isAdminPanel -> AdminPanelBottomBar(adminTab) { adminTab = it }
                                    else -> MainBottomBar(
                                        selectedTab = selectedTab,
                                        onTabSelected = { index ->
                                            if (index == 2) {
                                                if (SessionManager.isLoggedIn()) {
                                                    // Logout
                                                    SessionManager.logout()
                                                    isEmployeePanel = false
                                                    isAdminPanel = false
                                                    selectedTab = 0
                                                } else {
                                                    // Show login
                                                    showLoginScreen = true
                                                }
                                            } else {
                                                selectedTab = index
                                            }
                                        },
                                        isLoggedIn = SessionManager.isLoggedIn()
                                    )
                                }
                            }
                        }
                    ) { innerPadding ->
                        when {
                            isEmployeePanel && (sessionState.value == UserRole.STAFF || sessionState.value == UserRole.ADMIN) -> when (employeeTab) {
                                0 -> ShiftsScreen(menuDrawerState)
                                1 -> PayrollScreen(menuDrawerState)
                                2 -> EmployeeInfoScreen(menuDrawerState)
                            }
                            isAdminPanel && sessionState.value == UserRole.ADMIN -> when (adminTab) {
                                0 -> AssignShiftsScreen(menuDrawerState)
                                1 -> AdminPayrollScreen(menuDrawerState)
                                2 -> ManageMenuScreen(menuDrawerState)
                                3 -> ManageEmployeesScreen(menuDrawerState)
                            }
                            else -> {
                                if (showLoginScreen) {
                                    LoginScreen(
                                        onLoginSuccess = {
                                            showLoginScreen = false
                                            // Optionally, navigate to a different tab/panel after login
                                        }
                                    )
                                } else {
                                    when (selectedTab) {
                                        0 -> MenuScreen(menuDrawerState = menuDrawerState)
                                        1 -> OrderHistoryScreen()
                                        2 -> {} // Handled by showLoginScreen
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
//}

// Bottom bars for each panel
@Composable
fun MainBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit, isLoggedIn: Boolean) {
    val icons = listOf(
        Icons.Default.Home,          // Menu
        Icons.Default.List,          // Orders
        if (isLoggedIn) Icons.Default.Person else Icons.Default.Person // Login/Logout
    )
    val labels = listOf("Menu", "Orders", if (isLoggedIn) "Logout" else "Login")
    NavigationBar {
        labels.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = { Text(label) },
                icon = { Icon(icons[index], contentDescription = label) }
            )
        }
    }
}

@Composable
fun EmployeePanelBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val icons = listOf(
        Icons.Default.List,            // Shifts (alternative to Schedule)
        Icons.Default.ShoppingCart,  // Payroll (alternative to AttachMoney)
        Icons.Default.Info             // Info
    )
    val labels = listOf("Shifts", "Payroll", "Info")
    NavigationBar {
        labels.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = { Text(label) },
                icon = { Icon(icons[index], contentDescription = label) }
            )
        }
    }
}

@Composable
fun AdminPanelBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val icons = listOf(
        Icons.Default.List,           // Assign Shifts
        Icons.Default.ShoppingCart, // Payroll
        Icons.Default.Home,       // Manage Menu (alternative to RestaurantMenu)
        Icons.Default.Person          // Manage Employees (alternative to Group)
    )
    val labels = listOf("Assign Shifts", "Payroll", "Menu", "Employees")
    NavigationBar {
        labels.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                label = { Text(label) },
                icon = { Icon(icons[index], contentDescription = label) }
            )
        }
    }
}