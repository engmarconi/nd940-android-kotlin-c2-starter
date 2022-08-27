package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidDbModel::class], version = 1, exportSchema = false)
abstract class AsteroidRadarDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    companion object {
        @Volatile
        private lateinit var INSTANCE: AsteroidRadarDatabase

        fun getInstance(context: Context): AsteroidRadarDatabase {
            synchronized(AsteroidRadarDatabase::class.java){
                if (!::INSTANCE.isInitialized) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        AsteroidRadarDatabase::class.java,
                        "astroids").build()
                }
                return INSTANCE
            }
        }
    }
}