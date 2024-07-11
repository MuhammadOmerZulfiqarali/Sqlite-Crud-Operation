package com.example.sqlite_crud_operation

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class Helper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "StudentDB"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "students"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_SURNAME = "surname"
        private const val COLUMN_MARKS = "marks"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = ("CREATE TABLE $TABLE_NAME ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT, "
                + "$COLUMN_SURNAME TEXT, "
                + "$COLUMN_MARKS INTEGER)")
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertData(name: String, surname: String, marks: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_SURNAME, surname)
        contentValues.put(COLUMN_MARKS, marks)

        val result = db.insert(TABLE_NAME, null, contentValues)
        db.close()
        return result != -1L
    }

    @SuppressLint("Range")
    fun getAllData(): List<Student> {
        val studentList = mutableListOf<Student>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        try {
            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID))
                    val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                    val surname = cursor.getString(cursor.getColumnIndex(COLUMN_SURNAME))
                    val marks = cursor.getInt(cursor.getColumnIndex(COLUMN_MARKS))
                    val student = Student(id, name, surname, marks)
                    studentList.add(student)
                } while (cursor.moveToNext())
            }
        } catch (e: Exception) {
            Log.e("Helper", "Error while trying to get students from database", e)
        } finally {
            cursor?.close()
            db?.close()
        }
        return studentList
    }

    fun deleteData(student: Student): Boolean {
        val db = writableDatabase
        val success = db.delete(TABLE_NAME, "$COLUMN_ID=?", arrayOf(student.id.toString())) > 0
        db.close()
        return success
    }

    fun updateData(id: Int, name: String, surname: String, marks: Int): Boolean {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COLUMN_NAME, name)
        contentValues.put(COLUMN_SURNAME, surname)
        contentValues.put(COLUMN_MARKS, marks)

        val success = db.update(TABLE_NAME, contentValues, "$COLUMN_ID=?", arrayOf(id.toString())) > 0
        db.close()
        return success
    }
}