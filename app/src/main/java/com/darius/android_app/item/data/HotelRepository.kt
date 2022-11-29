package com.darius.android_app.item.data

import android.util.Log
import com.darius.android_app.item.data.local.ItemDao
import com.darius.android_app.item.data.remote.ItemEvent
import com.darius.android_app.item.data.remote.ItemService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.lang.Exception

class HotelRepository(
    private val itemService: ItemService,
    private val itemDao: ItemDao
) {
    val itemStream by lazy {
        itemDao.getAll()
    }

    suspend fun refresh() {
        try {
            val items = itemService.find()
            itemDao.deleteAll()
            items.forEach { itemDao.insert(it) }
        } catch (e: Exception) {
            Log.w("HotelsRepository", e)
        }
    }

    suspend fun update(item: Hotel): Hotel {
        val updatedItem = itemService.update(item._id, item)
        handleUpdatedItem(updatedItem)
        return updatedItem
    }

    suspend fun save(item: Hotel): Hotel {
        val createdItem = itemService.create(AddHotel(item))
        handleItemCreate(createdItem)
        return createdItem
    }

    suspend fun deleteAll() {
        itemDao.deleteAll()
    }

    private suspend fun handleUpdatedItem(item: Hotel) {
        itemDao.update(item)
    }

    private suspend fun handleItemCreate(item: Hotel) {
        itemDao.insert(item)
    }

//    fun setToken(token: String) {
//        itemService.
//    }
}