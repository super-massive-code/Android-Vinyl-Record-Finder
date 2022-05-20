package com.supermassivecode.vinylfinder.data.local

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class WantedRecordsWorkManager(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(appContext, workerParams)  {

    override fun doWork(): Result {
        return Result.success()
    }
}