package com.preonboarding.sensordashboard.presentation.view.sensor_history_measure

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.components.Legend
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.common.constant.Constants.X
import com.preonboarding.sensordashboard.common.constant.Constants.Y
import com.preonboarding.sensordashboard.common.constant.Constants.Z
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryMeasureBinding
import com.preonboarding.sensordashboard.presentation.viewmodel.SensorHistoryMeasureViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class SensorHistoryMeasureFragment :
    BaseFragment<FragmentSensorHistoryMeasureBinding>(R.layout.fragment_sensor_history_measure) {
    private val sensorEventListener: SensorEventListener by lazy {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                getSensorData(event)
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }
    }

    @Inject
    lateinit var sensorManager: SensorManager

    private lateinit var sensor: Sensor

    private val sensorHistoryMeasureViewModel: SensorHistoryMeasureViewModel by activityViewModels()
    private val format = DecimalFormat("#.####")

    private var xList = mutableListOf<Float>()
    private var yList = mutableListOf<Float>()
    private var zList = mutableListOf<Float>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectFlow()
        initView()
        initChart()
    }

    private fun registerSensorListener(listener: SensorEventListener, sensor: Sensor) {
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_FASTEST)
    }

    private fun initView() {
        binding.apply {
            vm = sensorHistoryMeasureViewModel
            tvHistoryMeasureStart.setOnClickListener {
                Toast.makeText(it.context, "측정 시작", Toast.LENGTH_SHORT).show()
                setMeasureButtonClickable(false)
                registerSensorListener(sensorEventListener, sensor)
                setTimer()
            }

            tvHistoryMeasureStop.setOnClickListener {
                Toast.makeText(it.context, "측정 중지", Toast.LENGTH_SHORT).show()
                setMeasureButtonClickable(true)
                sensorManager.unregisterListener(sensorEventListener)
                sensorHistoryMeasureViewModel.timerStop()
            }

            btnHistoryAccMeasure.setOnClickListener {
                refreshData()
                setSensorType(Sensor.TYPE_ACCELEROMETER)
                Toast.makeText(it.context, "ACC", Toast.LENGTH_SHORT).show()
            }

            btnHistoryGyroMeasure.setOnClickListener {
                refreshData()
                setSensorType(Sensor.TYPE_GYROSCOPE)
                Toast.makeText(it.context, "GYRO", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initChart() {
        // TODO 차트 세팅 필요 정호님에게 맡기겠습니다
        binding.chartView.apply {
            xAxis.apply {
                axisMaximum = 300f
                granularity = 1f
                isGranularityEnabled = true
            }
            axisRight.isEnabled = false
            axisLeft.apply {
                axisMaximum = 25f
                axisMinimum = -25f
            }
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        }
        //
        sensorHistoryMeasureViewModel.initLineData()
        binding.chartView.data = sensorHistoryMeasureViewModel.lineData
    }

    private fun collectFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sensorHistoryMeasureViewModel.currentMeasureValue.collect() { measureValue ->
                    sensorHistoryMeasureViewModel.addEntry(
                        measureValue.x,
                        measureValue.y,
                        measureValue.z
                    )
                    binding.chartView.apply {
                        notifyDataSetChanged()
                        invalidate()
                    }
                }
            }
        }
    }

    private fun refreshData() {
        binding.chartView.apply {
            data.clearValues()
            invalidate()
        }
        sensorHistoryMeasureViewModel.initLineData()
        binding.chartView.data = sensorHistoryMeasureViewModel.lineData
    }

    private fun setMeasureButtonClickable(state: Boolean) {
        binding.apply {
            btnHistoryAccMeasure.isClickable = state
            btnHistoryGyroMeasure.isClickable = state
        }
    }

    private fun setSensorType(sensorType: Int) {
        sensorManager.unregisterListener(sensorEventListener)
        sensor = sensorManager.getDefaultSensor(sensorType)
    }

    private fun setTimer() {
        var ss = 0
        var ms = 0
        val timerAction: () -> Unit = {
            if (ms == 10) {
                ms = 0
                ss++
                if (ss == 60) {
                    sensorHistoryMeasureViewModel.timerStop()
                }
            }
            //히스토리에 값 집어넣기

            binding.tvHistoryTimer.text = "${ss}:${ms++}"

            if (xList.isNotEmpty()) {
                Timber.d("현재 값 : ${xList[xList.lastIndex]},${yList[yList.lastIndex]},${zList[zList.lastIndex]}")
            }

        }
        sensorHistoryMeasureViewModel.timerStart(timerAction)
    }

    private fun getSensorData(event: SensorEvent) {
        sensorHistoryMeasureViewModel.updateCurrentMeasureValue(
            event.values[X],
            event.values[Y],
            event.values[Z]
        )

        xList.add(format.format(event.values[0]).toFloat())
        yList.add(format.format(event.values[1]).toFloat())
        zList.add(format.format(event.values[2]).toFloat())
    }

    override fun onStop() {
        sensorManager.unregisterListener(sensorEventListener)
        super.onStop()
    }
}