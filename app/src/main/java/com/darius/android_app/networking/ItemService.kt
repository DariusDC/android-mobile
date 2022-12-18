package com.darius.android_app.networking

import com.darius.android_app.model.AddHotel
import com.darius.android_app.model.Hotel
import retrofit2.http.*

interface ItemService {

    @GET("/api/item")
    suspend fun find(): List<Hotel>

    @Headers("Content-Type: application/json")
    @PUT("/api/item/{id}")
    suspend fun update(@Path("id") itemId: String?, @Body item: Hotel): Hotel;

    @Headers("Content-Type: application/json")
    @POST("/api/item")
    suspend fun create(@Body item: AddHotel): Hotel
}