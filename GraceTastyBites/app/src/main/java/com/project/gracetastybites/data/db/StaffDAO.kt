package com.project.gracetastybites.data.db

import android.database.sqlite.SQLiteDatabase

data class StaffDAO(
    val Id: Int,
    val FirstName: String,
    val LastName: String,
    val Role: String,
    val Email: String,
    val Password: String,
    val Wage: String
)

fun getStaffList(db: SQLiteDatabase): List<StaffDAO> {
    val staffList = mutableListOf<StaffDAO>()
    val cursor = db.rawQuery(
        "SELECT id, FirstName, LastName, role, email, password, wage FROM staff",
        null
    )
    cursor.use {
        while (it.moveToNext()) {
            val id = it.getInt(it.getColumnIndexOrThrow("id"))
            val FirstName = it.getString(it.getColumnIndexOrThrow("FirstName"))
            val lastname = it.getString(it.getColumnIndexOrThrow("LastName"))
            val role = it.getString(it.getColumnIndexOrThrow("role"))
            val email = it.getString(it.getColumnIndexOrThrow("email"))
            val password = it.getString(it.getColumnIndexOrThrow("password"))
            val wage = it.getInt(it.getColumnIndexOrThrow("wage")) != 0
            staffList.add(StaffDAO(id, LastName, lastname, role, email, password, wage))
        }
    }
    return staffList
}