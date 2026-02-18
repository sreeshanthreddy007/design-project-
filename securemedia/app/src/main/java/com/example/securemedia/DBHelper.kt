package com.example.securemedia

import android.content.*
import android.database.sqlite.*

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "users.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE users(" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT UNIQUE," +
                    "password TEXT)"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun register(username: String, password: String): Boolean {
        val cv = ContentValues()
        cv.put("username", username)
        cv.put("password", password)
        return writableDatabase.insert("users", null, cv) != -1L
    }

    fun login(username: String, password: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT * FROM users WHERE username=? AND password=?",
            arrayOf(username, password)
        )
        val ok = cursor.count > 0
        cursor.close()
        return ok
    }
}
