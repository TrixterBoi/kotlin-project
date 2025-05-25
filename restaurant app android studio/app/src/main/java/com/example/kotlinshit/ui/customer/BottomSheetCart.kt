package com.example.kotlinshit.ui.customer

import android.content.Context
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.example.kotlinshit.R

object BottomSheetCart {
    fun show(context: Context, cart: List<String>) {
        val bottomSheetDialog = BottomSheetDialog(context)
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_cart, null)
        val listViewCart = view.findViewById<ListView>(R.id.listViewCart)
        val cartAdapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, cart)
        listViewCart.adapter = cartAdapter

        bottomSheetDialog.setContentView(view)
        view.post {
            val height = context.resources.displayMetrics.heightPixels / 2
            view.layoutParams.height = height
            view.requestLayout()
        }
        bottomSheetDialog.show()
    }
}