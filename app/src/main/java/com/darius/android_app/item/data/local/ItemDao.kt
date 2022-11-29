package com.darius.android_app.item.data.local

import androidx.room.*
import com.darius.android_app.item.data.Hotel
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("select * from hotels")
    fun getAll(): Flow<List<Hotel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: Hotel)

    @Update
    suspend fun update(item: Hotel): Int

    @Query("delete from hotels")
    suspend fun deleteAll()
}