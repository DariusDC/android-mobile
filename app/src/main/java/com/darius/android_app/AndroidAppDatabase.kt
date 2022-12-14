package com.darius.android_app

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.darius.android_app.model.Hotel
import com.darius.android_app.dao.ItemDao

@Database(entities = [Hotel::class], version = 3)
abstract class AndroidAppDatabase : RoomDatabase() {
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AndroidAppDatabase? = null

        fun getDatabase(context: Context): AndroidAppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    AndroidAppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}