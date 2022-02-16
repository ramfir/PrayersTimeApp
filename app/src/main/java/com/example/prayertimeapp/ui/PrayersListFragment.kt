package com.example.prayertimeapp.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prayertimeapp.Contract.Contract
import com.example.prayertimeapp.Model.Prayer
import com.example.prayertimeapp.R
import com.example.prayertimeapp.databinding.FragmentPrayersListBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AndroidEntryPoint
class PrayersListFragment @Inject constructor(): Fragment(R.layout.fragment_prayers_list), Contract.View{

    private lateinit var binding: FragmentPrayersListBinding
    @Inject lateinit var prayersPresenter: Contract.Presenter
    private lateinit var prayersAdapter: PrayersAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPrayersListBinding.bind(view)

        setRecyclerView()

        prayersPresenter.loadPrayersTime()
        prayersPresenter.loadTextInfo()
    }

    private fun setRecyclerView() {
        binding.recyclerViewPrayers.adapter = PrayersAdapter(listOf())
        binding.recyclerViewPrayers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPrayers.setHasFixedSize(true)
    }

    override fun showPrayersTime(prayersTime: List<Prayer>) {
        prayersAdapter = PrayersAdapter(prayersTime)
        binding.recyclerViewPrayers.adapter = prayersAdapter
    }

    override fun highlightCurrentPrayer(position: Int) {
        prayersAdapter.currentPrayer = position
    }

    override fun changeImage(imageResourse: Int) {
        binding.imageViewPrayerTime.setImageResource(imageResourse)
    }

    override fun showTextInfo(info: String) {
        binding.infoTextView.text = info
    }

    override fun showErrorMessage(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_LONG).show()
    }

    override fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.progressBar.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        prayersPresenter.onViewDestroyed()
    }
}