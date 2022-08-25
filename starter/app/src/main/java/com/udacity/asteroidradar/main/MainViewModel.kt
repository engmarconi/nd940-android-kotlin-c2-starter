package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.NasaApiStatus
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.convertDateString
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.time.format.DateTimeFormatter
import java.util.*

enum class AsteroidFilter { TODAY, WEEK, LOCAL }
class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<NasaApiStatus>()
    val status: LiveData<NasaApiStatus>
        get() = _status

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _podUrl = MutableLiveData<String>()
    val podUrl: LiveData<String>
        get() = _podUrl

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails: LiveData<Asteroid>
        get() = _navigateToAsteroidDetails

    init {
        getAsteroidList(AsteroidFilter.WEEK)
        getPictureOfDay()
    }

    //////////////////////////////////////
     fun getAsteroidList(filter: AsteroidFilter) {
        viewModelScope.launch {
            try {
                var startDate = ""
                var endDate = ""
                var calendar = Calendar.getInstance()
                when (filter) {
                    AsteroidFilter.TODAY -> {
                        startDate = convertDateString(calendar.time)
                        endDate = startDate
                    }
                    AsteroidFilter.WEEK -> {
                        startDate = convertDateString(calendar.time)
                        calendar.add(Calendar.DATE, 7)
                        endDate = convertDateString(calendar.time)
                    }
                }
                _status.value = NasaApiStatus.LOADING
                if(filter != AsteroidFilter.LOCAL) {
                    var deferred = NasaApi.retrofitService.getAsteroidsAsync(startDate, endDate)
                    val response = deferred.await()
                    val json = JSONObject(response.body().toString())
                    _asteroidList.value = parseAsteroidsJsonResult(json)
                    _status.value = NasaApiStatus.SUCCESS
                }
                else{
                    _status.value = NasaApiStatus.SUCCESS
                }
            } catch (e: Exception) {
                _status.value = NasaApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }

     fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                _status.value = NasaApiStatus.LOADING
                var deferred = NasaApi.retrofitService.getPictureOfDayAsync()
                val pictureOfDay = deferred.await()
                _podUrl.value = pictureOfDay.url
                _status.value = NasaApiStatus.SUCCESS
            } catch (e: Exception) {
                _status.value = NasaApiStatus.ERROR
                e.printStackTrace()
            }
        }
    }
    //////////////////////////////////////////////

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToAsteroidDetails.value = null
    }


}