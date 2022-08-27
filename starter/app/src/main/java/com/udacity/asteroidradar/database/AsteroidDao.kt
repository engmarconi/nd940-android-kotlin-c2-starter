package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Insert
    suspend fun insert(asteroid: AsteroidDbModel)

    @Update
    suspend fun update(asteroid: AsteroidDbModel)

    @Query("select * from asteroid_table Order by close_approach_date ASC")
    suspend fun getAll() : List<AsteroidDbModel>?

    @Query("select * from asteroid_table where close_approach_date = :date Order by close_approach_date ASC")
    suspend fun getAll(date: Long) : List<AsteroidDbModel>?

    @Query("select * from asteroid_table where close_approach_date >= :start and close_approach_date <= :end Order by close_approach_date ASC")
    suspend fun getAll(start: Long, end: Long) : List<AsteroidDbModel>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: AsteroidDbModel)

    @Query("delete from asteroid_table where close_approach_date < :date")
    fun delete(date: Long)
}