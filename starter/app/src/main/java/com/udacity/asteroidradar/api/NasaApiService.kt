package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .baseUrl(Constants.BASE_URL)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .build()

interface NasaApiService {

    @GET("/neo/rest/v1/feed?api_key=" + Constants.API_KEY)
    fun getAsteroidsAsync(@Query("start_date") startDate: String, @Query("end_date") endDate: String) : Deferred<List<Asteroid>>

    @GET("/planetary/apod?api_key=" + Constants.API_KEY)
    fun getPictureOfDayAsync() : Deferred<PictureOfDay>

}