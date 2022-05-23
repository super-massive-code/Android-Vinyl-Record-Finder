package com.supermassivecode.vinylfinder.data.local

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WantedRecordsWorkManager(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams), KoinComponent {

    private val discogsWorker: DiscogsWantedRecordWorker by inject()

    override suspend fun doWork(): Result {

        var success = true
        withContext(Dispatchers.IO) {
            try {
                discogsWorker.doWork()
                // TODO: how to group all workers and doWork()? Do we need to?
                // TODO: shall we run them in parallel?
            } catch (ex: Exception) {
                success = false
            }
        }
        return if (success) {
            Result.success()
        } else {
            Result.failure()
        }
    }

    companion object {
        fun runOneTimeRequest(context: Context) {
            WorkManager.getInstance(context)
                .enqueue(
                    OneTimeWorkRequestBuilder<WantedRecordsWorkManager>()
                        .setConstraints(constraints())
                        .build()
                )
        }

        private fun constraints(): Constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
    }
}