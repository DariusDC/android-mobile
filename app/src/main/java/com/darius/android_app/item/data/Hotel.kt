package com.darius.android_app.item.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "hotels")
data class Hotel(
    @PrimaryKey val _id: String = "", val name: String = "", val price: Double = 0.0,
    val desc: String = "", val added: String = "", val available: Boolean = false,
    val imageURL: String? = null
)

data class AddHotel(
    val name: String = "", val price: Double = 0.0,
    val desc: String = "", val added: String = "", val available: Boolean = false,
    val imageURL: String? = null
) {
    constructor(h: Hotel) : this(h.name, h.price, h.desc, h.added, h.available, h.imageURL)
}