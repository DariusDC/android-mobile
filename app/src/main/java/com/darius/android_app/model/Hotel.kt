package com.darius.android_app.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotels")
class Hotel(
    @PrimaryKey var _id: String = "", val name: String = "", var price: Double = 0.0,
    val desc: String = "", val added: String = "", val available: Boolean = false,
    val imageURL: String? = null, val imageUri: String? = null,
) {
    fun toJson(): Map<String, Any> {
        val map = HashMap<String, Any>()
        map["_id"] = _id
        map["name"] = name
        map["price"] = price
        map["desc"] = desc
        map["added"] = added
        map["available"] = available
        map["imageURL"] = imageURL ?: ""
//        map["imageUri"] = imageUri ?: ""
        return map
    }

    fun toMyString(): String {
        val s: MutableList<String> = ArrayList()
        val map: Map<String, Any> = toJson()
        map.forEach {
            s.add("${it.key}egal${it.value}")
        }
        return s.joinToString(separator = "virgula")
    }

    companion object {
        fun fromJson(map: Map<String, Any>): Hotel = Hotel(
            map["_id"] as String,
            map["name"] as String,
            map["price"] as Double,
            map["desc"] as String,
            map["added"] as String,
            map["available"] as Boolean,
            map["imageURL"] as String,
//            map["imageUri"] as String,
        )

        fun fromMyList(s: String): Hotel {
            val map = s.split("virgula").associate {
                val (left, right) = it.split("egal")
                if (right.toDoubleOrNull() != null) left to right.toDouble()
                if (right.toBooleanStrictOrNull() != null) left to right.toBooleanStrict()
                left to right.toString()
            }
            return fromJson(map)
        }
    }
}

data class AddHotel(
    val name: String = "", val price: Double = 0.0,
    val desc: String = "", val added: String = "", val available: Boolean = false,
    val imageUri: String? = null
) {
    constructor(h: Hotel) : this(h.name, h.price, h.desc, h.added, h.available, h.imageUri)
}