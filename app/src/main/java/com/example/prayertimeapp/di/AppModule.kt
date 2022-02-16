package com.example.prayertimeapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import com.example.prayertimeapp.Contract.Contract
import com.example.prayertimeapp.Model.PrayersModel
import com.example.prayertimeapp.Presenter.PrayersListPresenter
import com.example.prayertimeapp.ui.PrayersListFragment
import com.google.gson.Gson
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.qualifiers.ApplicationContext


@Module
@InstallIn(FragmentComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindFragment(fragment: PrayersListFragment): Contract.View

    @Binds
    abstract fun bindPresenter(impl: PrayersListPresenter): Contract.Presenter

    @Binds
    abstract fun bindModel(impl: PrayersModel): Contract.Model
}

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    fun bindFragment(fragment: Fragment): PrayersListFragment {
        return fragment as PrayersListFragment
    }

    @Provides
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences("PrayersTime", Context.MODE_PRIVATE)

    @Provides
    fun provideGson(): Gson = Gson()
}
