package com.project.gracetastybites.data.db

import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues

data class InvoiceDAO(
    val Id: Int,
    val OrderId: Int,
    val TotalAmount: Double,
    val Paid: Boolean,
    val InvoiceTime: String
)

fun getInvoiceList(db: SQLiteDatabase): List<InvoiceDAO> {
    val invoiceList = mutableListOf<InvoiceDAO>()
    val cursor = db.rawQuery(
        "SELECT Id, OrderId, TotalAmount, Paid, InvoiceTime FROM Invoice",
        null
    )
    cursor.use {
        while (it.moveToNext()) {
            val Id = it.getInt(it.getColumnIndexOrThrow("Id"))
            val OrderId = it.getInt(it.getColumnIndexOrThrow("OrderId"))
            val TotalAmount = it.getDouble(it.getColumnIndexOrThrow("TotalAmount"))
            val Paid = it.getInt(it.getColumnIndexOrThrow("Paid")) != 0
            val InvoiceTime = it.getString(it.getColumnIndexOrThrow("InvoiceTime"))
            invoiceList.add(InvoiceDAO(Id, OrderId, TotalAmount, Paid, InvoiceTime))
        }
    }
    return invoiceList
}

fun insertInvoice(db: SQLiteDatabase, invoice: InvoiceDAO): Long {
    val values = ContentValues().apply {
        put("OrderId", invoice.OrderId)
        put("TotalAmount", invoice.TotalAmount)
        put("Paid", if (invoice.Paid) 1 else 0)
        put("InvoiceTime", invoice.InvoiceTime)
    }
    return db.insert("Invoice", null, values)
}

fun updateInvoice(db: SQLiteDatabase, invoice: InvoiceDAO): Int {
    val values = ContentValues().apply {
        put("OrderId", invoice.OrderId)
        put("TotalAmount", invoice.TotalAmount)
        put("Paid", if (invoice.Paid) 1 else 0)
        put("InvoiceTime", invoice.InvoiceTime)
    }
    return db.update("Invoice", values, "Id = ?", arrayOf(invoice.Id.toString()))
}

fun deleteInvoice(db: SQLiteDatabase, id: Int): Int {
    return db.delete("Invoice", "Id = ?", arrayOf(id.toString()))
}