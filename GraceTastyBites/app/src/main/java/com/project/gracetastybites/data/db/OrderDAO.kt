package com.project.gracetastybites.data.db

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues

data class OrderDAO(
    val Id: Int,
    val TableNumber: Int,
    val StaffId: Int,
    val OrderTime: String,
    val Status: String
)

fun getOrderList(db: SQLiteDatabase): List<OrderDAO> {
    val orderList = mutableListOf<OrderDAO>()
    val cursor = db.rawQuery(
        "SELECT Id, TableNumber, StaffId, OrderTime, Status FROM `Order`",
        null
    )
    cursor.use {
        while (it.moveToNext()) {
            val Id = it.getInt(it.getColumnIndexOrThrow("Id"))
            val TableNumber = it.getInt(it.getColumnIndexOrThrow("TableNumber"))
            val StaffId = it.getInt(it.getColumnIndexOrThrow("StaffId"))
            val OrderTime = it.getString(it.getColumnIndexOrThrow("OrderTime"))
            val Status = it.getString(it.getColumnIndexOrThrow("Status"))
            orderList.add(OrderDAO(Id, TableNumber, StaffId, OrderTime, Status))
        }
    }
    return orderList
}

fun insertOrder(db: SQLiteDatabase, order: OrderDAO): Long {
    val values = ContentValues().apply {
        put("TableNumber", order.TableNumber)
        put("StaffId", order.StaffId)
        put("OrderTime", order.OrderTime)
        put("Status", order.Status)
    }
    return db.insert("Order", null, values)
}

fun updateOrder(db: SQLiteDatabase, order: OrderDAO): Int {
    val values = ContentValues().apply {
        put("TableNumber", order.TableNumber)
        put("StaffId", order.StaffId)
        put("OrderTime", order.OrderTime)
        put("Status", order.Status)
    }
    return db.update("Order", values, "Id = ?", arrayOf(order.Id.toString()))
}

fun deleteOrder(db: SQLiteDatabase, id: Int): Int {
    return db.delete("Order", "Id = ?", arrayOf(id.toString()))
}