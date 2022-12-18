package com.darius.android_app.screens.login

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.darius.android_app.AndroidApplication
import com.darius.android_app.repo.AuthRepository
import com.darius.android_app.model.UserPreferences
import com.darius.android_app.repo.UserPreferencesRepository
import com.darius.android_app.screens.BaseViewModel
import kotlinx.coroutines.launch

data class LoginUIState(
    val isAuthenticating: Boolean = false,
    val authenticationError: Throwable? = null,
    val authenticationCompleted: Boolean = false,
    val token: String = ""
)

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    application: Application,
) : BaseViewModel(application) {

    var uiState: LoginUIState by mutableStateOf(LoginUIState())

    fun login(username: String, password: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isAuthenticating = true, authenticationError = null)
            val result = authRepository.login(username, password)
            if (result.isSuccess) {
                userPreferencesRepository.save(
                    UserPreferences(username, result.getOrNull()?.token ?: "")
                )
                uiState = uiState.copy(isAuthenticating = false, authenticationCompleted = true)
            } else {
                uiState = uiState.copy(
                    isAuthenticating = false,
                    authenticationError = result.exceptionOrNull()
                )
            }
        }
    }


    companion object {
        fun Factory(application: Application): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as AndroidApplication)
                LoginViewModel(
                    app.container.authRepository,
                    app.container.userPreferencesRepository,
                    application,
                )
            }
        }
    }
}