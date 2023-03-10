package com.example.newscanapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TestDB::class], version = 1, exportSchema = true)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun historyDao(): HistoryDao
}