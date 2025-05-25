package com.example.kotlinshit.ui.admin

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.kotlinshit.R

class AdminDashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Admin Dashboard"

        val btnStaffRole = findViewById<Button>(R.id.btnStaffRole)
        val btnShift = findViewById<Button>(R.id.btnShift)

        btnStaffRole.setOnClickListener {
            startActivity(Intent(this, AdminStaffActivity::class.java))
        }
        btnShift.setOnClickListener {
            startActivity(Intent(this, AdminShiftsActivity::class.java))
        }
    }
}