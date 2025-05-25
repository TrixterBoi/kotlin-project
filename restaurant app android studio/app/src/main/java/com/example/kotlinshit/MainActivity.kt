package com.example.kotlinshit

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private val foodMenu = listOf("Pizza", "Burger", "Sushi", "Pasta", "Salad")
    private val cart = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val navigationView = findViewById<NavigationView>(R.id.navigationView)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navigationView.setNavigationItemSelectedListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.nav_menu_nav_bar -> {
                    startActivity(Intent(this, MenuNavBar::class.java))
                    true
                }
                R.id.nav_cart_activity -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                else -> false
            }.also { drawerLayout.closeDrawers() }
        }

        val listViewMenu = findViewById<ListView>(R.id.listViewMenu)
        val buttonCart = findViewById<Button>(R.id.buttonCart)

        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foodMenu)
        listViewMenu.adapter = adapter

        listViewMenu.setOnItemClickListener { _, _, position, _ ->
            cart.add(foodMenu[position])
            buttonCart.text = "Cart (${cart.size})"
        }

        buttonCart.setOnClickListener {
            Toast.makeText(this, "Cart: ${cart.joinToString()}", Toast.LENGTH_SHORT).show()
        }
    }
}