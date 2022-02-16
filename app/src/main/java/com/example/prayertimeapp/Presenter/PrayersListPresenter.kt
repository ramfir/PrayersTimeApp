package com.example.prayertimeapp.Presenter

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.prayertimeapp.Contract.Contract
import com.example.prayertimeapp.Model.Prayer
import com.example.prayertimeapp.alarm.NotificationReceiver
import com.example.prayertimeapp.R
import com.example.prayertimeapp.utils.Constants.Companion.PRAYER_TIME_EXTRA
import com.example.prayertimeapp.utils.Constants.Companion.PRAYER_TITLE_EXTRA
import dagger.hilt.android.qualifiers.ApplicationContext
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class PrayersListPresenter @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prayersListView: Contract.View,
    private val prayersModel: Contract.Model): Contract.Presenter {

    private val subscriptions = CompositeDisposable()

    override fun loadPrayersTime() {
        val disposable = prayersModel.loadPrayersTime()
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { prayersListView.showProgressBar() }
            .doOnNext { prayersListView.hideProgressBar() }
            .doOnError {
                prayersListView.hideProgressBar()
                prayersListView.showErrorMessage("Check your internet connection")}
            .subscribe( { prayersTime ->
                if (prayersTime.isEmpty()) return@subscribe
                prayersListView.showPrayersTime(prayersTime)
                val currentPrayer = getCurrentPrayer(prayersTime)
                prayersListView.highlightCurrentPrayer(currentPrayer)
                changeImage(currentPrayer)
                setAlarms(prayersTime)
            }, {}, {})

        subscriptions.add(disposable)
    }

    private fun test() {
        val prayersTime = listOf(
            Prayer("fajr", "9:3"),
            Prayer("shuruq", "9:4"),
            Prayer("zuhr", "9:5"),
            Prayer("asr", "9:6"),
            Prayer("maghrib", "9:7"),
            Prayer("isha", "9:8"))
        prayersListView.hideProgressBar()
        prayersListView.showPrayersTime(prayersTime)
        val currentPrayer = getCurrentPrayer(prayersTime)
        prayersListView.highlightCurrentPrayer(currentPrayer)
        setAlarms(prayersTime)
    }

    private fun setAlarms(prayersTime: List<Prayer>) {
        lateinit var intent: Intent
        lateinit var pendingIntent: PendingIntent
        val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        prayersTime.forEach { prayer ->
            println("menii request code for ${prayer.title} is ${prayer.getHours()}")
            intent = Intent(context, NotificationReceiver::class.java).apply {
                putExtra(PRAYER_TITLE_EXTRA, prayer.title)
                putExtra(PRAYER_TIME_EXTRA, prayer.time)
            }
            pendingIntent = PendingIntent.getBroadcast(context, prayer.getHours(), intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, prayer.getCalendarTimeInMillis(), pendingIntent)
            //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, prayer.getCalendarTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent)
        }
    }

    private fun changeImage(currentPrayer: Int) {
        when (currentPrayer) {
            0 -> prayersListView.changeImage(R.drawable.fajr)
            1 -> prayersListView.changeImage(R.drawable.shuruq)
            2 -> prayersListView.changeImage(R.drawable.zuhr)
            3 -> prayersListView.changeImage(R.drawable.asr)
            4 -> prayersListView.changeImage(R.drawable.maghrib)
            5 -> prayersListView.changeImage(R.drawable.isha)
        }
    }

    private fun getCurrentPrayer(prayersTime: List<Prayer>): Int {
        val zoneId = ZoneId.of("Europe/Moscow")
        val currentTime = LocalTime.now(zoneId)//.plusHours(3)
        for (i in 0 until 6) {
            if (LocalTime.of(prayersTime[i].getHours(), prayersTime[i].getMinutes()).isAfter(currentTime)) {
                if (i == 0)
                    return 5
                return i-1
            }
        }
        return 5
    }

    override fun loadTextInfo() {
        val disposable = prayersModel.loadTextInfo()
            .subscribeOn(Schedulers.io())
            .timeout(5, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe( { textInfo ->
                prayersListView.showTextInfo(textInfo)
            }, {}, {})

        subscriptions.add(disposable)
    }

    override fun onViewDestroyed() {
        subscriptions.dispose()
    }
}