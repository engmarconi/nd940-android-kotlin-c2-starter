package com.udacity.asteroidradar.screens.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.models.Asteroid
import com.udacity.asteroidradar.helpers.convertDateLong
import com.udacity.asteroidradar.database.AsteroidRadarDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel(application: Application) : ViewModel() {

    private var today = 0L
    private var after7Days = 0L

    private val _asteroidList = MutableLiveData<List<Asteroid>>()
    val asteroidList: LiveData<List<Asteroid>>
       get() = _asteroidList

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails: LiveData<Asteroid>
        get() = _navigateToAsteroidDetails

    private val database = AsteroidRadarDatabase.getInstance(application)
    private val repository = AsteroidRepository(database)
    val fetchDataStatus = repository.fetchDataStatus
    val podStatus = repository.podStatus
    val pod = repository.pod

    init {
        var calendar = Calendar.getInstance()
        today = convertDateLong(calendar.time)
        calendar.add(Calendar.DATE, 7)
        after7Days = convertDateLong(calendar.time)

        refreshData()
        getPictureOfDay()
        getToday()
    }

    private fun refreshData(){
        viewModelScope.launch {
            repository.refreshAsteroidsData()
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            repository.getPictureOfDay()
        }
    }

    fun getToday() {
        viewModelScope.launch {
            _asteroidList.value = repository.getTodayData()
        }
    }

    fun getWeek() {
        viewModelScope.launch {
            _asteroidList.value = repository.getWeekData(today, after7Days)
        }
    }

    fun getAll() {
        viewModelScope.launch {
            _asteroidList.value = repository.getAllSavedData()
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToAsteroidDetails.value = null
    }


    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}