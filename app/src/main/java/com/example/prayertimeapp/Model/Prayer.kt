package com.example.prayertimeapp.Model

import java.util.*

data class Prayer(val title: String, val time: String) {

    fun getHours() = time.split(":")[0].toInt()
    fun getMinutes() = time.split(":")[1].toInt()

    fun getCalendarTimeInMillis(): Long {
        val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, getHours())
            set(Calendar.MINUTE, getMinutes())
            set(Calendar.SECOND, 0)
        }
        if (calendar.time.compareTo(Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1)

        println("menii $title ${calendar.get(Calendar.HOUR_OF_DAY)} ${calendar.get(Calendar.MINUTE)}")
        return calendar.timeInMillis
    }
}