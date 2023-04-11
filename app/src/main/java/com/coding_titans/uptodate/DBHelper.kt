package com.coding_titans.uptodate

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper


class DBHelper(context: Context?) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    /**
     * default function to create the table inside the DB
     */
    override fun onCreate(db: SQLiteDatabase) {
        val query = ("CREATE TABLE $TABLE_NAME (" +
                "$ID_COL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$NAME_COL TEXT, $EMAIL_COL TEXT, $PASSWORD_COL TEXT, $PHONE_COL TEXT);")

        db.execSQL(query)
    }

    /**
     * if the version of the db changes then this function will execute.
     */
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }


    /**
     * function to add user in the table
     * @param userName String - name of the user
     * @param userEmail String - email of the student
     * @param userPassword String - password of the user
     * @param userPhone String - phone number of the user
     *
     * @returns
     * 0 - user already exists
     * 1 - insertion successful
     * -1 - insertion failed
     */
    fun addNewUser(userName: String, userEmail: String, userPassword: String, userPhone: String): Int {
        val db = this.writableDatabase

        // check if user is already registered
        val userExists = checkUser(userEmail, userPassword)
        if(userExists == 1) {
            return 0
        }

        val values = ContentValues()

        values.put(NAME_COL, userName)
        values.put(EMAIL_COL, userEmail)
        values.put(PASSWORD_COL, userPassword)
        values.put(PHONE_COL, userPhone)

        // insert query
        val rowInserted = db.insert(TABLE_NAME, null, values).toInt()
        db.close()

        if(rowInserted != -1) {
            return 1
        } else {
            return -1
        }
    }


    /**
     * function to get the entries inside the users table
     * @return Cursor to access the rows
     */
    fun viewUsers(): Cursor? {
        val db = this.readableDatabase

        // return cursor
        return db.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }


    /**
     * function to update the user info
     * @param id String - id of the user to update
     * @param name String - updated name of the user
     * @param email String - updated email of the user
     * @param password String - updated password of the user
     * @param phone String - updated number of the user
     */
    fun updateUserInfo(id: String, name: String, email: String, password: String, phone: String) {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(NAME_COL, name)
        values.put(EMAIL_COL, email)
        values.put(PASSWORD_COL, password)
        values.put(PHONE_COL, phone)

        // update query
        db.update(TABLE_NAME, values, "$ID_COL=?", arrayOf(id))
        db.close()
    }


    /**
     * Verify user email and password in the database
     * @param email String - email/username of the user
     * @param password String - password of the user
     *
     * @returns
     * 0 => user not found
     * 1 => user exists
     */
    fun checkUser(email: String, password: String): Int {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $EMAIL_COL=? AND $PASSWORD_COL=?"
        db.rawQuery(query, arrayOf(email, password)).use {
            if(it.moveToFirst()) {
                return 1
            }
        }

        return 0
    }

    fun getUser(email: String): Cursor {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $EMAIL_COL=? LIMIT 1"

        return db.rawQuery(query, arrayOf(email))
    }


    /**
     * Check if user is logged in
     */
    fun checkLoggedIn(context: Activity): String {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE) ?: return ""
        return sharedPref.getString(SHARED_PREF_LOGGED_IN, "").toString()
    }

    /**
     * Set true when user is logged in
     */
    fun setLoggedIn(context: Activity, email: String) {
        val sharedPref = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString(SHARED_PREF_LOGGED_IN, email)
            apply()
        }
    }


    /**
     * constant values are defined for the Table
     */
    companion object {
        // below variable is for our database name.
        private const val DB_NAME = "usersdb"

        // below int is our database version
        private const val DB_VERSION = 1

        // below variable is for our table name.
        const val TABLE_NAME = "users_info"

        // below variable is for our id column.
        const val ID_COL = "user_id"

        // below variable is for our course name column
        const val NAME_COL = "user_name"

        // below variable is for email column
        const val EMAIL_COL = "email"

        // below variable is for password column
        const val PASSWORD_COL = "password"

        // below variable is for phone number column
        const val PHONE_COL = "phone_num"

        // below variable is for shared preference file
        const val SHARED_PREF_FILE = "shared_pref_file"

        const val SHARED_PREF_LOGGED_IN = "isLoggedIn"
    }
}