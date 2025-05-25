package com.example.kotlinshit.data.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.kotlinshit.data.model.StaffEntity

class StaffDao(context: Context) : SQLiteOpenHelper(context, "staff.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE staff (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, role TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS staff")
        onCreate(db)
    }

    fun getAllStaff(): List<StaffEntity> {
        val staffList = mutableListOf<StaffEntity>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM staff", null)
        while (cursor.moveToNext()) {
            staffList.add(
                StaffEntity(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    name = cursor.getString(cursor.getColumnIndexOrThrow("name")),
                    role = cursor.getString(cursor.getColumnIndexOrThrow("role"))
                )
            )
        }
        cursor.close()
        return staffList
    }

    fun insertStaff(name: String, role: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("name", name)
            put("role", role)
        }
        return db.insert("staff", null, values)
    }

    fun updateStaffRole(id: Int, newRole: String): Int {
        val db = writableDatabase
        val values = ContentValues().apply { put("role", newRole) }
        return db.update("staff", values, "id = ?", arrayOf(id.toString()))
    }

    fun deleteStaff(id: Int): Int {
        val db = writableDatabase
        return db.delete("staff", "id = ?", arrayOf(id.toString()))
    }
}