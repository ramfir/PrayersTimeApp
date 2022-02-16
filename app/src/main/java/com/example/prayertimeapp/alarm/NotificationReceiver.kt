package com.example.prayertimeapp.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.prayertimeapp.Model.Prayer
import com.example.prayertimeapp.utils.Constants.Companion.PRAYER_TIME_EXTRA
import com.example.prayertimeapp.utils.Constants.Companion.PRAYER_TITLE_EXTRA
import java.util.*

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val prayer = Prayer(
            intent?.getStringExtra(PRAYER_TITLE_EXTRA)!!,
            intent.getStringExtra(PRAYER_TIME_EXTRA)!!)

        setAnotherAlarm(context, prayer)

        val notificationHelper = NotificationHelper(context!!)
        notificationHelper.createNotification(prayer.title, prayer.time)
    }

    private fun setAnotherAlarm(context: Context?, prayer: Prayer) {
        val alarmManager: AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(PRAYER_TITLE_EXTRA, prayer.title)
            putExtra(PRAYER_TIME_EXTRA, prayer.time)
        }
        val pendingIntent = PendingIntent.getBroadcast(context, prayer.getHours(), intent, 0)
        /*val calendar: Calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, prayer.getHours())
            set(Calendar.MINUTE, prayer.getMinutes())
            set(Calendar.SECOND, 0)
        }
        if (calendar.time.compareTo(Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1)*/
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, prayer.getCalendarTimeInMillis(), pendingIntent)
    }
}