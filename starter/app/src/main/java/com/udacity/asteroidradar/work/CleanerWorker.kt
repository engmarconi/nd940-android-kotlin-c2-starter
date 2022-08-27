package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Operation
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import java.lang.Exception

class CleanerWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    companion object{
        const val WORK_NAME = "Old_data_cleaner"
    }
    override suspend fun doWork(): Result {
        val database = AsteroidRadarDatabase.getInstance(applicationContext)
        val repository = AsteroidRepository(database)
        return try {
            repository.removeOldData()
            Result.success()
        } catch (ex: Exception){
            Result.retry()
        }
    }
}