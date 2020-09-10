package com.marasigan.tableregistration.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class CustomersDBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "customerss_db"

        private val SQL_CREATE_ENTRIES = ("CREATE TABLE " + CustomerDB.TABLE_NAME + "("
                + CustomerDB.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + CustomerDB.COLUMN_NAME + " TEXT,"
                + CustomerDB.COLUMN_TABLE_NUM + " INTEGER)")

        private val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + CustomerDB.TABLE_NAME

    }

    override fun onCreate(db: SQLiteDatabase) {
        Log.d("DBHELPER", "Creating database")
//        db.execSQL(SQL_CREATE_ENTRIES)
        db.execSQL(CustomerDB.CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        Log.d("DBHELPER", "on upgrade")
        db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    fun insertCustomer(name: String, table_num: Int): Long {
        val db = writableDatabase
        val values = ContentValues()

        values.put(CustomerDB.COLUMN_NAME, name)
        values.put(CustomerDB.COLUMN_TABLE_NUM, table_num)

        val id = db.insert(CustomerDB.TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getCustomersByTable(table_num: Int): ArrayList<String> {
//        val query = ("SELECT name FROM " + CustomerDB.TABLE_NAME +
//                " WHERE table_num = " +table_num)
        val customerList: ArrayList<String> = arrayListOf()

//        val db = readableDatabase
//        var cursor: Cursor? = null
//        cursor = db.rawQuery(query, null)
//
//        while (cursor.moveToNext()){
//            customerList.add(cursor.getString(cursor.getColumnIndex(CustomerDB.TABLE_NAME)))
//        }
//        db.close()
//        return customerList


        val db = writableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("select * from " + CustomerDB.TABLE_NAME + " WHERE " + CustomerDB.COLUMN_TABLE_NUM + "='" + table_num + "'", null)
        } catch (e: SQLiteException) {
            // if table not yet present, create it
            db.execSQL(SQL_CREATE_ENTRIES)
            return ArrayList()
        }

        var customerName: String
        if (cursor!!.moveToFirst()) {
            while (cursor.isAfterLast == false) {
                customerName = cursor.getString(cursor.getColumnIndex(CustomerDB.COLUMN_NAME))
                customerList.add(customerName)
                cursor.moveToNext()
            }
        }
        return customerList

    }

    fun deleteAll() {
        val db = writableDatabase
        db.delete(CustomerDB.TABLE_NAME, null, null)
        db.close()
    }
}