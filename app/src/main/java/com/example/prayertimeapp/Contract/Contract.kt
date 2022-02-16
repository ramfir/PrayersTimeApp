package com.example.prayertimeapp.Contract

import com.example.prayertimeapp.Model.Prayer
import io.reactivex.Observable
import io.reactivex.Single

interface Contract {

    interface Presenter {
        fun loadPrayersTime()
        fun onViewDestroyed()
        fun loadTextInfo()
    }

    interface Model {
        fun loadPrayersTime(): Observable<List<Prayer>>
        fun loadTextInfo(): Observable<String>
    }

    interface View {
        fun showPrayersTime(prayersTime: List<Prayer>)
        fun highlightCurrentPrayer(position: Int)
        fun changeImage(imageResourse: Int)
        fun showTextInfo(info: String)
        fun showErrorMessage(msg: String)
        fun showProgressBar()
        fun hideProgressBar()
    }
}