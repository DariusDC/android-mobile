package com.darius.android_app.core.data.remote

import android.util.Log
import com.darius.android_app.core.TAG
import okhttp3.Interceptor
import okhttp3.Response

class TokenInterceptor constructor() : Interceptor {
    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val originalUrl = original.url
        if (token == null) {
            Log.d(TAG, "intercept: Token is null")
            return chain.proceed(original)
        }

        val requestBuilder = original.newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .url(originalUrl)
        val request = requestBuilder.build()
        Log.d(TAG, "intercept: Authorization bearer token added")
        return chain.proceed(request)
    }
}