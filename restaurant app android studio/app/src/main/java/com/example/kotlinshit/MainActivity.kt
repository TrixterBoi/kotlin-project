package com.example.kotlinshit

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable



class MainActivity : AppCompatActivity() {
    private val foodMenu = listOf("Pizza", "Burger", "Sushi", "Pasta", "Salad")
    private val cart = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
