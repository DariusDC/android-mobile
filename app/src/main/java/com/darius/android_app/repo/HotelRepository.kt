package com.darius.android_app.repo

import android.util.Log
import com.darius.android_app.core.TAG
import com.darius.android_app.dao.ItemDao
import com.darius.android_app.model.AddHotel
import com.darius.android_app.model.Hotel
import com.darius.android_app.networking.ItemService
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
            Log.d(TAG, "refresh: $items")
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

    private suspend fun handleItemDeleted(item: Hotel) {
        Log.d(TAG, "handleItemDeleted - todo $item")
    }

    suspend fun handleUpdatedItem(item: Hotel): Hotel {
        itemDao.update(item)
        return item
    }

    suspend fun handleItemCreate(item: Hotel): Hotel {
        item._id = "invalid"
        itemDao.insert(item)
        return item
    }
}