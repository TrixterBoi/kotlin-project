package com.project.gracetastybites.data.db

import android.database.sqlite.SQLiteDatabase

data class StaffDAO(
    val Id: Int,
    val FirstName: String,
    val LastName: String,
    val Role: String,
    val Email: String,
    val Password: String,
    val Wage: Int
)

fun getStaffList(db: SQLiteDatabase): List<StaffDAO> {
    val staffList = mutableListOf<StaffDAO>()
    val cursor = db.rawQuery(
        "SELECT Id, FirstName, LastName, Role, Email, Password, Wage FROM StaffMember",
        null
    )
    cursor.use {
        while (it.moveToNext()) {
            val Id = it.getInt(it.getColumnIndexOrThrow("id"))
            val FirstName = it.getString(it.getColumnIndexOrThrow("FirstName"))
            val LastName = it.getString(it.getColumnIndexOrThrow("LastName"))
            val Role = it.getString(it.getColumnIndexOrThrow("Role"))
            val Email = it.getString(it.getColumnIndexOrThrow("Email"))
            val Password = it.getString(it.getColumnIndexOrThrow("Password"))
            val Wage = it.getInt(it.getColumnIndexOrThrow("Wage"))
            staffList.add(StaffDAO(Id, FirstName, LastName, Role, Email, Password, Wage))
        }
    }
    return staffList
}