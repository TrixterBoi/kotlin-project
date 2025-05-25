package com.project.gracetastybites.data.db

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues

data class ShiftDAO(
    val Id: Int,
    val StaffId: Int,
    val StartTime: String,
    val EndTime: String
)

fun getShiftList(db: SQLiteDatabase): List<ShiftDAO> {
    val shiftList = mutableListOf<ShiftDAO>()
    val cursor = db.rawQuery(
        "SELECT Id, StaffId, StartTime, EndTime FROM Shift",
        null
    )
    cursor.use {
        while (it.moveToNext()) {
            val Id = it.getInt(it.getColumnIndexOrThrow("Id"))
            val StaffId = it.getInt(it.getColumnIndexOrThrow("StaffId"))
            val StartTime = it.getString(it.getColumnIndexOrThrow("StartTime"))
            val EndTime = it.getString(it.getColumnIndexOrThrow("EndTime"))
            shiftList.add(ShiftDAO(Id, StaffId, StartTime, EndTime))
        }
    }
    return shiftList
}

fun insertShift(db: SQLiteDatabase, shift: ShiftDAO): Long {
    val values = ContentValues().apply {
        put("StaffId", shift.StaffId)
        put("StartTime", shift.StartTime)
        put("EndTime", shift.EndTime)
    }
    return db.insert("Shift", null, values)
}

fun updateShift(db: SQLiteDatabase, shift: ShiftDAO): Int {
    val values = ContentValues().apply {
        put("StaffId", shift.StaffId)
        put("StartTime", shift.StartTime)
        put("EndTime", shift.EndTime)
    }
    return db.update("Shift", values, "Id = ?", arrayOf(shift.Id.toString()))
}

fun deleteShift(db: SQLiteDatabase, id: Int): Int {
    return db.delete("Shift", "Id = ?", arrayOf(id.toString()))
}