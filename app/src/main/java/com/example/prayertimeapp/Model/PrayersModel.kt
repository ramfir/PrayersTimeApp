package com.example.prayertimeapp.Model

import android.content.SharedPreferences
import com.example.prayertimeapp.Contract.Contract
import com.example.prayertimeapp.utils.Constants
import com.google.gson.Gson
import io.reactivex.Observable
import org.jsoup.Jsoup
import java.io.IOException
import java.text.DecimalFormat
import javax.inject.Inject

class PrayersModel @Inject constructor(): Contract.Model {

    private var prayersTime = mutableListOf<Prayer>()
    private var textInfo = ""
    @Inject lateinit var sharedPref: SharedPreferences
    @Inject lateinit var gson: Gson

    override fun loadPrayersTime(): Observable<List<Prayer>> =
        Observable.create { emitter ->
            try {
                getPrayersFromSharedPref()
                emitter.onNext(prayersTime)
                getPrayersTimeFromDUMRF()
                //getPrayersTimeFromUmmaRu()
                emitter.onNext(prayersTime)
                getAsrFromVreamyaNamaza()
                emitter.onNext(prayersTime)
                savePrayers()
                emitter.onComplete()
            } catch (exception: IOException) {
                Thread.sleep(1000) // without it, subscribers not always will get values from onNext
                emitter.onError(exception)
            }
        }

    private fun getPrayersFromSharedPref() {
        val prayersInJson = sharedPref.getString(Constants.PREF_TAG, "")
        if (prayersInJson != "")
            prayersTime = gson.fromJson(prayersInJson, Array<Prayer>::class.java)!!.toMutableList()
    }

    private fun getPrayersTimeFromDUMRF() {
        val doc = Jsoup.connect(Constants.DUMRF_URL).get()
        val timesWithTitles = doc.select("div#namaz").map { it.text() }[0].split(" ")
        parseTimes(timesWithTitles)
    }

    private fun parseTimes(timesWithTitles: List<String>) {
        prayersTime.clear()
        val f = DecimalFormat("00")
        var h = ""
        var m = ""
        for (i in 7 until 24 step 3) {
            h = f.format(timesWithTitles[i+1].split(":")[0].toInt())
            m = f.format(timesWithTitles[i+1].split(":")[1].toInt())
            prayersTime.add(Prayer(timesWithTitles[i], "$h:$m"))
        }
    }

    private fun getAsrFromVreamyaNamaza() {
        val doc = Jsoup.connect(Constants.VREMYA_NAMAZA_URL)
            .get()
        val asr = doc.select("div.right.floated.content.prayerTime .prayerInfo")
            .first()
            .text()
            .split(' ')
            .get(2)
            .replace(")", "")
            .split(":")
        prayersTime[3] = Prayer(prayersTime[3].title, "${asr[0]}:${asr[1]}")
    }

    private fun savePrayers() {
        val prayersInJson = gson.toJson(prayersTime)
        with(sharedPref.edit()) {
            putString(Constants.PREF_TAG, prayersInJson)
            apply()
        }
    }

    override fun loadTextInfo(): Observable<String> =
        Observable.create { emitter ->
            try {
                getTextInfoFromSharedPref()
                emitter.onNext(textInfo)
                getTextInfoFromDUMRF()
                emitter.onNext(textInfo)
                saveTextInfo()
                emitter.onComplete()
            } catch (exception: IOException) {
                Thread.sleep(1000) // without it, subscribers not always will get values from onNext
                emitter.onError(exception)
            }
        }

    private fun getTextInfoFromSharedPref() {
        textInfo = sharedPref.getString(Constants.TEXT_INFO, "")!!
    }

    private fun getTextInfoFromDUMRF() {
        val doc = Jsoup.connect(Constants.DUMRF_URL).get()
        val timesWithTitles = doc.select("div#namaz").map { it.text() }[0].split(" ")
        textInfo = timesWithTitles.joinToString(" ", limit = 7, truncated = "")
    }

    private fun saveTextInfo() {
        with(sharedPref.edit()) {
            putString(Constants.TEXT_INFO, textInfo)
            apply()
        }
    }

    private fun getPrayersTimeFromUmmaRu() {
        val doc = Jsoup.connect(Constants.UMMA_URL).get()
        val timesWithTitles =
            doc.select(".timenamaz__events-item .timenamaz__events-item-caption, .timenamaz__events-item-value")
                .map { it.text() }
        prayersTime.clear()
        for (i in 0 until timesWithTitles.size step 2) {
            val f = DecimalFormat("00")
            val time = "${f.format(timesWithTitles[i].split(":")[0].trim().toInt())}:${f.format(
                timesWithTitles[i].split(":")[1].trim().toInt()
            )}"
            prayersTime.add(Prayer(timesWithTitles[i + 1], time))
        }
    }
}