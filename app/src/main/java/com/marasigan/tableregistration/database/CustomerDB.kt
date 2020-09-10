package com.marasigan.tableregistration.database

class CustomerDB(val id: Int, val name: String, val table_num: Int) {

    companion object {
        var TABLE_NAME = "customers"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TABLE_NUM = "table_num"
        val CREATE_TABLE = ("CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_TABLE_NUM + " INTEGER)")
    }

}