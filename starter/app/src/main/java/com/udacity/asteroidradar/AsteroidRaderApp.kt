package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.work.CleanerWorker
import com.udacity.asteroidradar.work.CleanerWorker.Companion.WORK_NAME
import com.udacity.asteroidradar.work.RefreshDataWorker
import com.udacity.asteroidradar.work.RefreshDataWorker.Companion.DOWNLOAD_WORK_NAME
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidRadarApp : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        initWorkers()
    }

    private fun initWorkers() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .build()

        val refreshRepeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()

        val cleanRepeatingRequest = PeriodicWorkRequestBuilder<CleanerWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints).build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DOWNLOAD_WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,
            refreshRepeatingRequest
        )

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            WORK_NAME, ExistingPeriodicWorkPolicy.KEEP,
            cleanRepeatingRequest
        )
    }
}