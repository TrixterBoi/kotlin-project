package com.example.kotlinshit

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinshit.viewmodel.CartItem
import com.example.kotlinshit.ui.customer.CartAdapter

class CartActivity : AppCompatActivity() {
    private val cartItems = mutableListOf<CartItem>()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_activity)

        cartAdapter = CartAdapter(cartItems)
        val recyclerView = findViewById<RecyclerView>(R.id.cartRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = cartAdapter

        intent.getStringExtra("itemName")?.let { itemName ->
            addItemToCart(itemName)
        }

        findViewById<Button>(R.id.checkoutButton).setOnClickListener {
            // Handle checkout
        }

    }

    // Call this method when a menu item is clicked
    fun addItemToCart(name: String) {
        val existing = cartItems.find { it.name == name }
        if (existing != null) {
            existing.quantity++
        } else {
            cartItems.add(CartItem(name, 1))
        }
        cartAdapter.notifyDataSetChanged()
    }
}