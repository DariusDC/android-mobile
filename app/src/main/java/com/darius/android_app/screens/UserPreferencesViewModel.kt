package com.darius.android_app.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darius.android_app.AndroidApplication
import com.darius.android_app.model.UserPreferences
import com.darius.android_app.repo.UserPreferencesRepository
import kotlinx.coroutines.launch

class UserPreferencesViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {
    var uiState: UserPreferences by mutableStateOf(UserPreferences())
        private set

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            userPreferencesRepository.userPreferencesStream.collect { userPreferences ->
                uiState = userPreferences
            }
        }
    }

    fun save(userPreferences: UserPreferences) {
        viewModelScope.launch {
            userPreferencesRepository.save(userPreferences)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AndroidApplication)
                UserPreferencesViewModel(app.container.userPreferencesRepository)
            }
        }
    }
}