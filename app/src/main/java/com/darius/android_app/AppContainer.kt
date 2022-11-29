package com.darius.android_app

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.darius.android_app.auth.AuthRepository
import com.darius.android_app.auth.remote.AuthDataSource
import com.darius.android_app.core.data.UserPreferencesRepository
import com.darius.android_app.core.data.remote.Api
import com.darius.android_app.item.data.HotelRepository
import com.darius.android_app.item.data.remote.ItemService


val Context.userPreferencesDataStore by preferencesDataStore(name = "user_preferences")

class AppContainer(private val content: Context) {

    private val itemService: ItemService = Api.retrofit.create(ItemService::class.java)
    private val authDataSource: AuthDataSource = AuthDataSource()

    private val database: AndroidAppDatabase by lazy { AndroidAppDatabase.getDatabase(content) }

    val hotelRepository: HotelRepository by lazy {
        HotelRepository(itemService, database.itemDao())
    }

    val authRepository: AuthRepository by lazy {
        AuthRepository(authDataSource)
    }

    val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(content.userPreferencesDataStore)
    }
}