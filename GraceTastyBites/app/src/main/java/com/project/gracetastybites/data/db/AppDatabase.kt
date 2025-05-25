package com.project.gracetastybites.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase

class AppDatabase private constructor(context: Context, dbName: String) {

    private val db: SQLiteDatabase = DatabaseHelper(context, dbName).openDatabase()

    val database: SQLiteDatabase
        get() = db

    val menuItems: List<MenuDAO>
        get() = getMenuItems(db)

    val staffList: List<StaffDAO>
        get() = getStaffList(db)

    val orderList: List<OrderDAO>
        get() = getOrderList(db)

    val shiftList: List<ShiftDAO>
        get() = getShiftList(db)

    val invoiceList: List<InvoiceDAO>
        get() = getInvoiceList(db)

    fun close() = db.close()

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context, dbName: String = "KotlinDB.db"): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = AppDatabase(context.applicationContext, dbName)
                INSTANCE = instance
                instance
            }
        }

        fun closeInstance() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}