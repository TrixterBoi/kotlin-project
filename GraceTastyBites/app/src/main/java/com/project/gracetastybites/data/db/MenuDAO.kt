package com.project.gracetastybites.data.db

import android.database.sqlite.SQLiteDatabase

data class MenuDAO(
    val Id: Int,
    val Name: String,
    val Category: String,
    val Description: String,
    val Price: Double,
    val Available: Boolean
)

fun getMenuItems(db: SQLiteDatabase): List<MenuDAO> {
    val menuList = mutableListOf<MenuDAO>()
    val cursor = db.rawQuery(
        "SELECT Id, Name, Category, Description, Price, Available FROM MenuItem",
        null
    )
    cursor.use {
        while (it.moveToNext()) {
            val Id = it.getInt(it.getColumnIndexOrThrow("Id"))
            val Name = it.getString(it.getColumnIndexOrThrow("Name"))
            val Category = it.getString(it.getColumnIndexOrThrow("Category"))
            val Description = it.getString(it.getColumnIndexOrThrow("Description"))
            val Price = it.getDouble(it.getColumnIndexOrThrow("Price"))
            val Available = it.getInt(it.getColumnIndexOrThrow("Available")) != 0
            menuList.add(MenuDAO(Id, Name, Category, Description, Price, Available))
        }
    }
    return menuList
}