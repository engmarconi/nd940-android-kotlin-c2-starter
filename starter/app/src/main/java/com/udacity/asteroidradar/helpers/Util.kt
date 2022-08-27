package com.udacity.asteroidradar.helpers

import java.text.SimpleDateFormat
import java.util.*

fun convertDateString(date: Date): String {
    return SimpleDateFormat("yyyy-MM-dd")
        .format(date).toString()
}

fun convertDateLong(date: Date): Long {
    val string = convertDateString(date)
    return SimpleDateFormat("yyyy-MM-dd")
        .parse(string).time
}


fun convertStringDateToLong(date: String): Long {
    return SimpleDateFormat("yyyy-MM-dd")
        .parse(date).time
}

fun convertLongDateToString(date: Long): String {
    return SimpleDateFormat("yyyy-MM-dd")
        .format(Date(date))
}
