package com.project.gracetastybites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
                var isEmployeePanel by remember { mutableStateOf(false) }
                var isAdminPanel by remember { mutableStateOf(false) }
                var selectedTab by remember { mutableStateOf(0) }
                var employeeTab by remember { mutableStateOf(0) }
                var adminTab by remember { mutableStateOf(0) }
                val menuDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                var shouldCloseDrawer by remember { mutableStateOf(false) }

                // Drawer items
                val drawerItems = when {
                    isEmployeePanel -> listOf(Triple("Return Home", Icons.Default.Home, "Return Home"))
                    isAdminPanel -> listOf(Triple("Return Home", Icons.Default.Home, "Return Home"))
                    else -> listOf(
                        Triple("Employee Panel", Icons.Default.Face, "Employee Management"),
                        Triple("Admin Panel", Icons.Default.Info, "Admin Management")
                    )
                }

                // Close drawer after navigation
                LaunchedEffect(shouldCloseDrawer) {
                    if (shouldCloseDrawer) {
                        menuDrawerState.close()
                        shouldCloseDrawer = false
                    }
                }

                ModalNavigationDrawer(
                    drawerState = menuDrawerState,
                    drawerContent = {
                        NavDrawer(
                            drawerItems = drawerItems,
                            onItemClick = { label ->
                                when (label) {
                                    "Employee Panel" -> {
                                        isEmployeePanel = true
                                        isAdminPanel = false
                                        employeeTab = 0
                                    }
                                    "Admin Panel" -> {
                                        isAdminPanel = true
                                        isEmployeePanel = false
                                        adminTab = 0
                                    }
                                    "Return Home" -> {
                                        isEmployeePanel = false
                                        isAdminPanel = false
                                        selectedTab = 0
                                    }
                                }
                                shouldCloseDrawer = true
                            },
                            onLoginClick = { /* not used */ },
                            drawerState = menuDrawerState
                        )
                    }
                ) {
                    Scaffold(
                        bottomBar = {
                            if (!menuDrawerState.isOpen) {
                                when {
                                    isEmployeePanel -> EmployeePanelBottomBar(employeeTab) { employeeTab = it }
                                    isAdminPanel -> AdminPanelBottomBar(adminTab) { adminTab = it }
                                    else -> MainBottomBar(selectedTab) { selectedTab = it }
                                }
                            }
                        }
                    ) { innerPadding ->
                        when {
                            isEmployeePanel -> when (employeeTab) {
                                0 -> ShiftsScreen(menuDrawerState)
                                1 -> PayrollScreen(menuDrawerState)
                                2 -> EmployeeInfoScreen(menuDrawerState)
                            }
                            isAdminPanel -> when (adminTab) {
                                0 -> AssignShiftsScreen(menuDrawerState)
                                1 -> AdminPayrollScreen(menuDrawerState)
                                2 -> ManageMenuScreen(menuDrawerState)
                                3 -> ManageEmployeesScreen(menuDrawerState)
                            }
                            else -> when (selectedTab) {
                                0 -> MenuScreen(menuDrawerState = menuDrawerState)
                                1 -> OrderHistoryScreen()
                                2 -> LoginScreen()
                            }
                        }
                    }
                }
            }
        }
    }
}

// Bottom bars for each panel
@Composable
fun MainBottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    val icons = listOf(
        Icons.Default.Home,          // Menu
        Icons.Default.List,          // Orders
        Icons.Default.Person         // Login
    )
    val labels = listOf("Menu", "Orders", "Login")
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
        Icons.Default.List,          // Shifts
        Icons.Default.ShoppingCart,  // Payroll
        Icons.Default.Info           // Info
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
        Icons.Default.List,         // Assign Shifts
        Icons.Default.ShoppingCart, // Payroll
        Icons.Default.Home,         // Manage Menu
        Icons.Default.Person        // Manage Employees
    )
    val labels = listOf("Assign Shifts", "Payroll", "Manage Menu", "Manage Employees")
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