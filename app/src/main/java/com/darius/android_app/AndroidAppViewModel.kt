package com.darius.android_app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darius.android_app.model.UserPreferences
import com.darius.android_app.repo.UserPreferencesRepository
import com.darius.android_app.repo.HotelRepository
import kotlinx.coroutines.launch

class AndroidAppViewModel(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val hotelRepository: HotelRepository
) : ViewModel() {

    fun logout() {
        viewModelScope.launch {
            hotelRepository.deleteAll()
            userPreferencesRepository.save(UserPreferences())
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AndroidApplication)
                AndroidAppViewModel(
                    app.container.userPreferencesRepository,
                    app.container.hotelRepository
                )
            }
        }
    }

}