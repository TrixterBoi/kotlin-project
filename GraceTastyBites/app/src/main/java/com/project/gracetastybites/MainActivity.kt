package com.project.gracetastybites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.project.gracetastybites.ui.menu.MenuScreen
import com.project.gracetastybites.ui.orders.OrderHistoryScreen
import com.project.gracetastybites.ui.account.AccountDetailsScreen
import com.project.gracetastybites.ui.theme.GraceTastyBitesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraceTastyBitesTheme {
                var selectedTab by remember { mutableStateOf(0) }
                val tabs = listOf("Menu", "Order History", "Account Details")
                val tabIcons = listOf(Icons.Default.Home, Icons.Default.List, Icons.Default.Person)
                val menuDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                Scaffold(
                    bottomBar = {
                        if (!menuDrawerState.isOpen) {
                            NavigationBar {
                                tabs.forEachIndexed { index, label ->
                                    NavigationBarItem(
                                        icon = { Icon(tabIcons[index], contentDescription = label) },
                                        label = { Text(label) },
                                        selected = selectedTab == index,
                                        onClick = { selectedTab = index }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    when (selectedTab) {
                        0 -> MenuScreen(menuDrawerState = menuDrawerState)
                        1 -> OrderHistoryScreen()
                        2 -> AccountDetailsScreen()
                    }
                }
            }
        }
    }
}