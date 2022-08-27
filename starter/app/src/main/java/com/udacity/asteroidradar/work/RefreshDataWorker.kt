package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import java.lang.Exception

class RefreshDataWorker(appContext: Context, params: WorkerParameters) : CoroutineWorker(appContext, params)
{
    companion object{
        const val DOWNLOAD_WORK_NAME = "DOWNLOAD_WORK_NAME"
    }
    override suspend fun doWork(): Result {
        val database = AsteroidRadarDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.downloadTodayData()
            Result.success()
        } catch (ex: Exception){
            Result.retry()
        }
    }
}