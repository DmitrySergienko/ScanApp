package com.newscanapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "new_db2")
data class TestDB(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val emirates_id:String
)