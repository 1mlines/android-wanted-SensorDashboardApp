package com.preonboarding.sensordashboard.presentation.view.sensor_history_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.sensordashboard.databinding.ItemHistoryRvBinding
import com.preonboarding.sensordashboard.domain.model.SensorHistory

class HistoryPagingAdapter :
    PagingDataAdapter<SensorHistory, HistoryPagingAdapter.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemHistoryRvBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    class ViewHolder(
        private val binding: ItemHistoryRvBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SensorHistory) {
            with(binding) {
                data = item
                executePendingBindings()
            }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<SensorHistory>() {
            override fun areItemsTheSame(oldItem: SensorHistory, newItem: SensorHistory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: SensorHistory,
                newItem: SensorHistory
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}