package com.example.kotlinshit.ui.admin

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinshit.R
import com.example.kotlinshit.data.db.StaffDao
import com.example.kotlinshit.data.model.StaffEntity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AdminStaffActivity : AppCompatActivity() {
    private lateinit var staffDao: StaffDao
    private lateinit var adapter: StaffAdapter
    private val staffList = mutableListOf<StaffEntity>()
    private val roles = listOf("chef", "waiter", "cashier", "manager")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_staff)

        staffDao = StaffDao(this)
        staffList.addAll(staffDao.getAllStaff())

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Admin Staff Management"

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewStaff)
        adapter = StaffAdapter(staffList, ::showMenu)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabAddStaff).setOnClickListener {
            showAddStaffDialog()
        }
    }

    private fun showMenu(view: View, staff: StaffEntity) {
        val popup = PopupMenu(this, view)
        popup.menuInflater.inflate(R.menu.menu_staff_item, popup.menu)
        popup.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_add -> {
                    showAddStaffDialog()
                    true
                }
                R.id.action_update -> {
                    showUpdateRoleSheet(staff)
                    true
                }
                R.id.action_delete -> {
                    staffDao.deleteStaff(staff.id)
                    staffList.clear()
                    staffList.addAll(staffDao.getAllStaff())
                    adapter.notifyDataSetChanged()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }

    private fun showAddStaffDialog() {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_staff_add_bottom_sheet, null)
        val nameEdit = view.findViewById<EditText>(R.id.editStaffName)
        val spinner = view.findViewById<Spinner>(R.id.spinnerRoles)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        view.findViewById<Button>(R.id.buttonAddStaff).setOnClickListener {
            val name = nameEdit.text.toString().trim()
            val role = spinner.selectedItem as String
            if (name.isNotEmpty()) {
                staffDao.insertStaff(name, role)
                staffList.clear()
                staffList.addAll(staffDao.getAllStaff())
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            } else {
                Toast.makeText(this, "Name required", Toast.LENGTH_SHORT).show()
            }
        }
        dialog.setContentView(view)
        dialog.show()
    }

    private fun showUpdateRoleSheet(staff: StaffEntity) {
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_staff_update_bottom_sheet, null)
        val spinner = view.findViewById<Spinner>(R.id.spinnerRoles)
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spinner.setSelection(roles.indexOf(staff.role))

        view.findViewById<Button>(R.id.buttonUpdateRole).setOnClickListener {
            val newRole = spinner.selectedItem as String
            staffDao.updateStaffRole(staff.id, newRole)
            staffList.clear()
            staffList.addAll(staffDao.getAllStaff())
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }
        dialog.setContentView(view)
        dialog.show()
    }
}

class StaffAdapter(
    private val staffList: List<StaffEntity>,
    private val onMenuClick: (View, StaffEntity) -> Unit
) : RecyclerView.Adapter<StaffAdapter.StaffViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StaffViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_staff, parent, false)
        return StaffViewHolder(view)
    }

    override fun onBindViewHolder(holder: StaffViewHolder, position: Int) {
        val staff = staffList[position]
        holder.name.text = staff.name
        holder.role.text = staff.role
        holder.menu.setOnClickListener { v -> onMenuClick(v, staff) }
    }

    override fun getItemCount() = staffList.size

    class StaffViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.textStaffName)
        val role: TextView = view.findViewById(R.id.textStaffRole)
        val menu: ImageButton = view.findViewById(R.id.buttonMenu)
    }
}