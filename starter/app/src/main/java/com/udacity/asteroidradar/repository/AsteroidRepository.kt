package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.*
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NasaApiStatus
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDbModel
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.main.AsteroidFilter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.*

class AsteroidRepository(private val database: AsteroidRadarDatabase) {

    private val today = convertDateLong(Calendar.getInstance().time)
    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status
    private val _pod = MutableLiveData<PictureOfDay>()
    val pod: LiveData<PictureOfDay>
        get() = _pod

    suspend fun refreshAsteroidsData() {
        withContext(Dispatchers.IO) {
            //Get week data
            var calendar = Calendar.getInstance()
            val startDate = convertDateString(calendar.time)
            calendar.add(Calendar.DATE, 7)
            val endDate = convertDateString(calendar.time)
            var deferred = NasaApi.retrofitService.getAsteroidsAsync(startDate, endDate)
            val response = deferred.await()
            val json = JSONObject(response.body().toString())
            val asteroidsList = parseAsteroidsJsonResult(json)
            database.asteroidDao.insertAll(*asteroidsList.asDatabaseModel())
        }
    }

    suspend fun getPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                _status.postValue(NasaApiStatus.LOADING)
                var deferred = NasaApi.retrofitService.getPictureOfDayAsync()
                val pictureOfDay = deferred.await()
                _pod.postValue(pictureOfDay)
                _status.postValue(NasaApiStatus.SUCCESS)
            } catch (e: Exception) {
                _status.postValue(NasaApiStatus.ERROR)
                e.printStackTrace()
            }
        }
    }

    suspend fun removeOldData() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.delete(Calendar.getInstance().time.time)
        }
    }

    suspend fun getTodayData(): List<Asteroid> {
        return database.asteroidDao.getAll(today)?.asDomainModel() ?: listOf()
    }

    suspend fun getWeekData(start: Long, end: Long): List<Asteroid> {
        return database.asteroidDao.getAll(start, end)?.asDomainModel() ?: listOf()
    }

    suspend fun getAllSavedData(): List<Asteroid> {
        return database.asteroidDao.getAll()?.asDomainModel() ?: listOf()
    }
}

fun ArrayList<Asteroid>.asDatabaseModel(): Array<AsteroidDbModel> {
    return map {
        AsteroidDbModel(
            id = it.id,
            absoluteMagnitude = it.absoluteMagnitude,
            codename = it.codename,
            closeApproachDate = convertStringDateToLong(it.closeApproachDate),
            distanceFromEarth = it.distanceFromEarth,
            relativeVelocity = it.relativeVelocity,
            estimatedDiameter = it.estimatedDiameter,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toTypedArray()
}

fun List<AsteroidDbModel>.asDomainModel(): List<Asteroid> {
    return map {
        Asteroid(
            id = it.id,
            absoluteMagnitude = it.absoluteMagnitude,
            codename = it.codename,
            closeApproachDate = convertLongDateToString(it.closeApproachDate),
            distanceFromEarth = it.distanceFromEarth,
            relativeVelocity = it.relativeVelocity,
            estimatedDiameter = it.estimatedDiameter,
            isPotentiallyHazardous = it.isPotentiallyHazardous
        )
    }.toList()
}
