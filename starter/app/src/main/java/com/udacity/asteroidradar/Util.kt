package com.udacity.asteroidradar

import java.text.SimpleDateFormat
import java.util.*

fun convertDateString(date: Date): String {
    return SimpleDateFormat("yyyy-MM-dd")
        .format(date).toString()
}
