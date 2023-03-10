package com.example.newscanapp.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM new_db2")
    fun readAll(): Flow<List<TestDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(history: TestDB)

    @Delete
    fun deleteItem(history: TestDB)

    @Query("DELETE FROM new_db2")
    fun deleteAll()

    @Query("SELECT * FROM new_db2 where id = :id")
    fun getByID(id:String): TestDB

    @Query("select * from new_db2 where name = :name")
    fun getByName(name:String): TestDB

}