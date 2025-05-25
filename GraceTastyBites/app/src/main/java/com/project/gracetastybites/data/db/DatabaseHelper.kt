package com.project.gracetastybites.data.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class DatabaseHelper(private val context: Context, private val dbName: String) {

    private val dbPath: String
        get() = context.getDatabasePath(dbName).path

    fun openDatabase(): SQLiteDatabase {
        copyDatabaseIfNeeded()
        return SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READWRITE)
    }

    private fun copyDatabaseIfNeeded() {
        val dbFile = File(dbPath)
        if (!dbFile.exists()) {
            dbFile.parentFile?.mkdirs()
            try {
                context.assets.open(dbName).use { inputStream ->
                    FileOutputStream(dbFile).use { outputStream ->
                        val buffer = ByteArray(1024)
                        var length: Int
                        while (inputStream.read(buffer).also { length = it } > 0) {
                            outputStream.write(buffer, 0, length)
                        }
                        outputStream.flush()
                    }
                }
            } catch (e: IOException) {
                throw RuntimeException("Error copying database from assets", e)
            }
        }
    }
}