package com.darius.android_app.auth

import com.darius.android_app.auth.remote.AuthDataSource
import com.darius.android_app.auth.remote.TokenHolder
import com.darius.android_app.auth.remote.User
import com.darius.android_app.core.data.remote.Api

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