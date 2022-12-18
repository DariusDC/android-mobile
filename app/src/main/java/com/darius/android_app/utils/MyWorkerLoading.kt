package com.darius.android_app.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit.SECONDS
import androidx.work.workDataOf
import com.darius.android_app.model.Hotel

class MyWorkerLoading(
    context: Context,
    workerParameters: WorkerParameters,
) : Worker(context, workerParameters) {
    override fun doWork(): Result {
        for (i in 1..10) {
            if (isStopped) {
                break
            }
            print("Job $i")
            SECONDS.sleep(1)
            setProgressAsync(workDataOf("progress" to i))
        }
        return Result.success()
    }
}