package com.example.pgr208.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

// DatabaseHelper class extends SQLITEOpenHelper and override onCreate onUpgrade methods.
// onCreate is called when db is created for the first time
// onUpgrade is called when db needs to be upgraded to a new version.
// The DatabaseHelper class has a parameter as 'context'. In Kotlin a class context is a global object that provides
// system level access services and resources, such as strings, layouts and drawables.
class DatabaseHelper(context: Context, factory: SQLiteDatabase.CursorFactory? ) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION ) {

    companion object {

        // DB name
        private const val DATABASE_NAME = "recipeDatabase"
        // DB version
        private const val DATABASE_VERSION = 1
        // Table name
        private const val TABLE_NAME = "recipes"
        // Column names
        private const val KEY_ID = "id"
        private const val KEY_LABEL = "label"

        private var instance: DatabaseHelper? = null

        fun getInstance(context: Context): DatabaseHelper {
            if (instance == null) {
                instance = DatabaseHelper(context,null)
            }
            return instance!!
        }
    }

    // Creating table
    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_RECIPES_TABLE = ("CREATE TABLE "
                + TABLE_NAME + "("
                + KEY_ID
                + " INTEGER PRIMARY KEY,"
                + KEY_LABEL
                + " TEXT"
                + ")" )
        db?.execSQL(CREATE_RECIPES_TABLE)
    }
    // Upgrading database
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Drop older table if exist
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        // Create table
        onCreate(db)
    }

    fun getLabel(): Cursor? {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }
}