package com.darius.android_app.repo

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.darius.android_app.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) {
    private object PreferencesKey {
        val username = stringPreferencesKey("username")
        val token = stringPreferencesKey("token")
    }

    var userPreferencesStream: Flow<UserPreferences> = dataStore.data
        .catch { ex ->
            if (ex is IOException) {
                emit(emptyPreferences())
            } else {
                throw ex
            }
        }.map { mapUserPreferences(it) }


    suspend fun save(userPreferences: UserPreferences) {
        dataStore.edit { it ->
            it[PreferencesKey.username] = userPreferences.username
            it[PreferencesKey.token] = userPreferences.token
        }
    }

    private fun mapUserPreferences(preferences: Preferences) = UserPreferences(
        username = preferences[PreferencesKey.username] ?: "",
        token = preferences[PreferencesKey.token] ?: ""
    )
}