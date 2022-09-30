package com.preonboarding.sensordashboard.presentation.view.sensor_history_play

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.common.constant.ViewName
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryPlayBinding
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SensorHistoryPlayFragment :
    BaseFragment<FragmentSensorHistoryPlayBinding>(R.layout.fragment_sensor_history_play) {

    private val sensorHistoryPlayViewModel: SensorHistoryPlayViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.vm = sensorHistoryPlayViewModel

        val args: SensorHistoryPlayFragmentArgs by navArgs()

        initLineChart(args.history, args.viewName)
        initView(args.history, args.viewName)
    }

    private fun initView(history: SensorHistory, viewName: ViewName) {

        when(viewName) {
            ViewName.PLAY -> {
                binding.tvSubTitle.text = getString(R.string.play)
            }
            else -> {
                binding.tvTimer.visibility = View.GONE
                binding.btnControl.visibility = View.GONE
                binding.tvSubTitle.text = getString(R.string.view)
            }
        }

        binding.tvDate.text = history.publishedAt

        binding.btnControl.setOnClickListener {
            if (sensorHistoryPlayViewModel.isPlay) {
                stopPlay()
            } else {
                binding.btnControl.setImageResource(R.drawable.ic_stop_48)

                val chartAnimation: (Int) -> Unit = { index ->
                    binding.lineChart.notifyDataSetChanged()
                    binding.lineChart.setVisibleXRangeMaximum(20F)
                    binding.lineChart.moveViewToX(index.toFloat())
                    binding.lineChart.invalidate()
                }

                setTimer()
                sensorHistoryPlayViewModel.playStart(chartAnimation) { stopPlay() }
            }
        }
    }

    private fun initLineChart(history: SensorHistory, viewName: ViewName) {

        sensorHistoryPlayViewModel.initLineData(history, viewName)

        binding.lineChart.apply {
            data = sensorHistoryPlayViewModel.lineData
            setScaleEnabled(true)
            description.isEnabled = false
            setPinchZoom(false)
            legend.isEnabled = false
            axisLeft.setDrawLabels(false)
            axisRight.setDrawLabels(false)
            xAxis.setDrawGridLines(true)
            xAxis.setDrawLabels(false)
            setVisibleXRangeMaximum(20F)
            invalidate()
        }
    }

    private fun setTimer() {
        var ss = sensorHistoryPlayViewModel.timerSs
        var ms = sensorHistoryPlayViewModel.timerMs

        val timerAction: () -> Pair<Int, Int> = {
            if (ms == 10) {
                ms = 0
                ss++
            }

            binding.tvTimer.text = "${ss}.${ms++}"

            Pair(ss, ms)
        }
        sensorHistoryPlayViewModel.timerStart(timerAction)
    }

    private fun stopPlay() {
        binding.btnControl.setImageResource(R.drawable.ic_play_48)
        sensorHistoryPlayViewModel.playStop()
        sensorHistoryPlayViewModel.timerStop()
    }
}
