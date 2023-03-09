package com.example.newscanapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


/**
 * Database class with a singleton INSTANCE object.
 */
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ScanDataBase: RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var Instance: ScanDataBase? = null

        fun getDatabase(context: Context): ScanDataBase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, ScanDataBase::class.java, "item_database")
                    // Setting this option in your app's database builder means that Room
                    // permanently deletes all data from the tables in your database when it
                    // attempts to perform a migration with no defined migration path.
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }

}