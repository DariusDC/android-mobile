package com.darius.android_app.repo

import com.darius.android_app.networking.AuthDataSource
import com.darius.android_app.model.TokenHolder
import com.darius.android_app.model.User
import com.darius.android_app.networking.Api

class AuthRepository(private val authDataSource: AuthDataSource) {

    fun clearToken() {
        Api.tokenInterceptor.token = null
    }

    suspend fun login(username: String, password: String): Result<TokenHolder> {
        val user = User(username, password)
        val result = authDataSource.login(user)
        if (result.isSuccess) {
            Api.tokenInterceptor.token = result.getOrNull()?.token
        }
        return result
    }

}