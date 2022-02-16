package com.example.prayertimeapp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prayertimeapp.Model.Prayer
import com.example.prayertimeapp.databinding.ItemPrayerTimeBinding

class PrayersAdapter(private val prayersList: List<Prayer>): RecyclerView.Adapter<PrayersAdapter.PrayerViewHolder>() {

    var currentPrayer = RecyclerView.NO_POSITION
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class PrayerViewHolder(private val binding: ItemPrayerTimeBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(prayer: Prayer) {
            binding.apply {
                textViewPrayerTitle.text = prayer.title
                textViewPrayerTime.text = prayer.time
            }
        }

        fun highlight() {
            binding.apply {
                prayerTimeContainer.setBackgroundColor(Color.parseColor("#FF6200EE"))
                textViewPrayerTitle.setTextColor(Color.WHITE)
                textViewPrayerTime.setTextColor(Color.WHITE)
            }
        }

        fun deHighlight() {
            binding.apply {
                prayerTimeContainer.setBackgroundColor(Color.WHITE)
                textViewPrayerTitle.setTextColor(Color.BLACK)
                textViewPrayerTime.setTextColor(Color.BLACK)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrayerViewHolder {
        val binding = ItemPrayerTimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return PrayerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PrayerViewHolder, position: Int) {
        val currentItem = prayersList[position]
        holder.bind(currentItem)

        if (position == currentPrayer) {
            holder.highlight()
        } else {
            holder.deHighlight()
        }
    }

    override fun getItemCount(): Int  = prayersList.size
}