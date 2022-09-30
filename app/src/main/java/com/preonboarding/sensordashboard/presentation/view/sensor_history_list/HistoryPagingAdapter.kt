package com.preonboarding.sensordashboard.presentation.view.sensor_history_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ItemHistoryRvBinding
import com.preonboarding.sensordashboard.domain.model.SensorHistory

class HistoryPagingAdapter(private val itemClickListener: (SensorHistory) -> Unit) :
    PagingDataAdapter<SensorHistory, HistoryPagingAdapter.ViewHolder>(diffCallback) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_history_rv,
                parent, false
            ),
            itemClickListener
        )
    }

    class ViewHolder(
        private val binding: ItemHistoryRvBinding,
        private val itemClickListener: (SensorHistory) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                rvBtnDel.setOnClickListener {
                    data?.let {
                        itemClickListener(it)
                    }
                }
            }
        }

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