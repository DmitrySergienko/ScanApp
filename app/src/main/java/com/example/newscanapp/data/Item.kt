package com.example.newscanapp.data

import androidx.room.PrimaryKey

data class Item(

    @PrimaryKey(autoGenerate = true) //id will be unique for each item
    val id: Int = 0,
    val name: String,
    val emirates_id: Int
)
