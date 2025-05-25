// AdminStaffActivity.kt
package com.example.kotlinshit.ui.admin

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinshit.R
import com.example.kotlinshit.data.model.StaffEntity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlin.reflect.KFunction2

data class Staff(val name: String, var role: String)

class AdminShiftsActivity : AppCompatActivity() {
    private val staffList = mutableListOf(
        Staff("Alice", "chef"),
        Staff("Bob", "waiter"),
        Staff("Charlie", "cashier"),
        Staff("Diana", "manager")
    )
    private lateinit var adapter: StaffAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_staff)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Admin Staff Management"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStaff)
        adapter = StaffAdapter(staffList, ::showUpdateRoleSheet)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddStaff).setOnClickListener {
            // Handle add staff
            Toast.makeText(this, "Add Staff clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showUpdateRoleSheet(staff: Staff) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_staff_update_bottom_sheet, null)
        val spinner = view.findViewById<Spinner>(R.id.spinnerRoles)
        val roles = listOf("chef", "waiter", "cashier", "manager")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spinner.setSelection(roles.indexOf(staff.role))

        view.findViewById<Button>(R.id.buttonUpdateRole).setOnClickListener {
            staff.role = spinner.selectedItem as String
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }
}

class StaffAdapter(
    private val staffList: MutableList<StaffEntity>,
    private val onUpdate: KFunction2<View, StaffEntity, Unit>
) : RecyclerView.Adapter<StaffAdapter.StaffViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_staff, parent, false)
        return StaffViewHolder(view)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staff = staffList[position]
        holder.name.text = staff.name
        holder.role.text = staff.role
        holder.menu.setOnClickListener { v ->
            val popup = PopupMenu(v.context, v)
            popup.menuInflater.inflate(R.menu.menu_staff_item, popup.menu)
            popup.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_add -> {
                        Toast.makeText(v.context, "Add clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.action_update -> {
                        onUpdate(staff)
                        true
                    }
                    R.id.action_delete -> {
                        Toast.makeText(v.context, "Delete clicked", Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }

    override fun getItemCount() = staffList.size

    class StaffViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.textStaffName)
        val role: TextView = view.findViewById(R.id.textStaffRole)
        val menu: ImageButton = view.findViewById(R.id.buttonMenu)
    }
}