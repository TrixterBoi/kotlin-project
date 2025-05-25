package com.project.gracetastybites.data.db

import android.content.ContentValues
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

fun insertStaff(db: SQLiteDatabase, staff: StaffDAO): Long {
    val values = ContentValues().apply {
        put("FirstName", staff.FirstName)
        put("LastName", staff.LastName)
        put("Role", staff.Role)
        put("Email", staff.Email)
        put("Password", staff.Password)
        put("Wage", staff.Wage)
    }
    return db.insert("StaffMember", null, values)
}

fun updateStaff(db: SQLiteDatabase, staff: StaffDAO): Int {
    val values = ContentValues().apply {
        put("FirstName", staff.FirstName)
        put("LastName", staff.LastName)
        put("Role", staff.Role)
        put("Email", staff.Email)
        put("Password", staff.Password)
        put("Wage", staff.Wage)
    }
    return db.update("StaffMember", values, "Id = ?", arrayOf(staff.Id.toString()))
}

fun deleteStaff(db: SQLiteDatabase, id: Int): Int {
    return db.delete("StaffMember", "Id = ?", arrayOf(id.toString()))
}

fun authenticateStaff(db: SQLiteDatabase, email: String, password: String): StaffDAO? {
    val cursor = db.rawQuery(
        "SELECT Id, FirstName, LastName, Role, Email, Password, Wage FROM StaffMember WHERE Email = ? AND Password = ?",
        arrayOf(email, password)
    )
    cursor.use {
        if (it.moveToFirst()) {
            return StaffDAO(
                it.getInt(it.getColumnIndexOrThrow("Id")),
                it.getString(it.getColumnIndexOrThrow("FirstName")),
                it.getString(it.getColumnIndexOrThrow("LastName")),
                it.getString(it.getColumnIndexOrThrow("Role")),
                it.getString(it.getColumnIndexOrThrow("Email")),
                it.getString(it.getColumnIndexOrThrow("Password")),
                it.getInt(it.getColumnIndexOrThrow("Wage"))
            )
        }
    }
    return null
}