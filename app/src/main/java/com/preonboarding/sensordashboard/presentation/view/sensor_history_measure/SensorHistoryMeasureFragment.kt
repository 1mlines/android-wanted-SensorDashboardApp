package com.preonboarding.sensordashboard.presentation.view.sensor_history_measure

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.components.Legend
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.common.constant.Constants.X
import com.preonboarding.sensordashboard.common.constant.Constants.Y
import com.preonboarding.sensordashboard.common.constant.Constants.Z
import com.preonboarding.sensordashboard.common.extension.setSelectedColor
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryMeasureBinding
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.presentation.viewmodel.SensorHistoryMeasureViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class SensorHistoryMeasureFragment :
    BaseFragment<FragmentSensorHistoryMeasureBinding>(R.layout.fragment_sensor_history_measure) {
    private val sensorEventListener: SensorEventListener by lazy {
        object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> {
                        getSensorData(event)
                        sensorType = "ACCELEROMETER"
                    }
                    Sensor.TYPE_GYROSCOPE -> {
                        getSensorData(event)
                        sensorType = "GYROSCOPE"
                    }
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}
        }
    }

    @Inject
    lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private val sensorHistoryMeasureViewModel: SensorHistoryMeasureViewModel by viewModels()
    private val format = DecimalFormat("#.####")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
    private lateinit var history: SensorHistory
    private lateinit var sensorType: String
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
        sensorManager.registerListener(listener, sensor, 100000)
    }

    private fun initView() {
        binding.apply {
            vm = sensorHistoryMeasureViewModel

            tvMenu.setOnClickListener {
                if (xList.isNullOrEmpty()) {
                    Toast.makeText(it.context, "데이터가 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(it.context, "저장 완료", Toast.LENGTH_SHORT).show()
                    val period = binding.tvHistoryTimer.text
                    sensorHistoryMeasureViewModel.timerStop()
                    history = SensorHistory(
                        0,
                        sensorType,
                        dateFormat.format(Date(System.currentTimeMillis())),
                        period.toString(),
                        xList.toList(),
                        yList.toList(),
                        zList.toList()
                    )
                    sensorHistoryMeasureViewModel.saveSensorHistory(history)
                    Timber.d("history : $history")
                    sensorManager.unregisterListener(sensorEventListener)
                    xList.clear()
                    yList.clear()
                    zList.clear()

                }
            }

            tvHistoryMeasureStart.setOnClickListener {
                if (::sensor.isInitialized) {
                    Toast.makeText(requireContext(), "측정 시작", Toast.LENGTH_SHORT).show()
                    setMeasureButtonClickable(false)
                    registerSensorListener(sensorEventListener, sensor)
                    setTimer()
                    tvHistoryMeasureStop.isClickable = true
                } else {
                    Toast.makeText(requireContext(), "센서를 선택해 주세요", Toast.LENGTH_SHORT).show()
                }
            }


            tvHistoryMeasureStop.setOnClickListener {
                Toast.makeText(it.context, "측정 중지", Toast.LENGTH_SHORT).show()
                setMeasureButtonClickable(true)
                sensorManager.unregisterListener(sensorEventListener)
                sensorHistoryMeasureViewModel.timerStop()
            }

            btnHistoryAccMeasure.setOnClickListener {
                btnHistoryAccMeasure.setSelectedColor(true)
                btnHistoryGyroMeasure.setSelectedColor(false)
                refreshData()
                setSensorType(Sensor.TYPE_ACCELEROMETER)
            }

            btnHistoryGyroMeasure.setOnClickListener {
                btnHistoryGyroMeasure.setSelectedColor(true)
                btnHistoryAccMeasure.setSelectedColor(false)
                refreshData()
                setSensorType(Sensor.TYPE_GYROSCOPE)
            }
        }
    }

    private fun initChart() {
        // TODO 차트 세팅 필요 정호님에게 맡기겠습니다
        binding.chartView.apply {
            xAxis.apply {
                axisMaximum = 600.0f
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
                        if (data.dataSets[X].xMax == 600.0f) {
                            sensorManager.unregisterListener(sensorEventListener)
                            sensorHistoryMeasureViewModel.timerStop()
                        }

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
        xList.clear()
        yList.clear()
        zList.clear()
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

                if ((ss == 60)) {
                    sensorHistoryMeasureViewModel.timerStop()
                    sensorManager.unregisterListener(sensorEventListener)
                }
            }

            binding.tvHistoryTimer.text = "${ss}.${ms++}"

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

        xList.add(event.values[0])
        yList.add(event.values[1])
        zList.add(event.values[2])
    }

    override fun onStop() {
        sensorManager.unregisterListener(sensorEventListener)
        sensorHistoryMeasureViewModel.timerStop()
        sensorHistoryMeasureViewModel.refreshCurrentMeasureValue()
        super.onStop()
    }

}