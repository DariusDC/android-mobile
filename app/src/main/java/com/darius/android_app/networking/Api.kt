package com.darius.android_app.networking

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {

    private val url = "172.20.10.3:3000";
    private val httpUrl = "http://$url"
    val wsUrl = "ws://$url"

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    val tokenInterceptor = TokenInterceptor()

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().apply {
        this.addInterceptor(tokenInterceptor)
    }.build()

    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(httpUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

}