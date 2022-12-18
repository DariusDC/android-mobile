package com.darius.android_app.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.darius.android_app.networking.Api
import com.darius.android_app.networking.ItemService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MyWorker(
    context: Context,
    workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        withContext(Dispatchers.IO) {
        val itemService: ItemService = Api.retrofit.create(ItemService::class.java)
        val items = itemService.find()
            return@withContext Result.success(
                workDataOf(
                    "items" to items.map { it.toMyString() }
                        .toList().joinToString(separator = "newElement")
                ),
            )
        }
        return Result.retry()
//        return Result.failure()
    }
}