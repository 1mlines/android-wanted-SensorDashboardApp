package com.preonboarding.sensordashboard.presentation.view.sensor_history_measure

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.common.base.BaseFragment
import com.preonboarding.sensordashboard.databinding.FragmentSensorHistoryMeasureBinding
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import com.preonboarding.sensordashboard.presentation.viewmodel.SensorHistoryMeasureViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.text.DecimalFormat
import javax.inject.Inject

@AndroidEntryPoint
class SensorHistoryMeasureFragment :
    BaseFragment<FragmentSensorHistoryMeasureBinding>(R.layout.fragment_sensor_history_measure) {
    private lateinit var sensorEventListener: SensorEventListener

    @Inject
    lateinit var sensorManager: SensorManager

    private lateinit var sensor: Sensor
    private val sensorHistoryMeasureViewModel: SensorHistoryMeasureViewModel by viewModels()
    private val format = DecimalFormat("#.####")
    private lateinit var history: SensorHistory
    private var xList = mutableListOf<Float>()
    private var yList = mutableListOf<Float>()
    private var zList = mutableListOf<Float>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
        initListener()
        initView()

    }

    private fun initListener() {
        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                //수정?
                when (event.sensor.type) {
                    Sensor.TYPE_ACCELEROMETER -> getSensorData(event)
                    Sensor.TYPE_GYROSCOPE -> getSensorData(event)
                }
            }

            override fun onAccuracyChanged(p0: Sensor?, p1: Int) {}

        }
    }


    private fun registerSensorListener(listener: SensorEventListener, sensor: Sensor) {
        sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }


    private fun initView() {
        //저장btn

        binding.apply {
            tvHistoryMeasureStart.setOnClickListener {
                Toast.makeText(it.context, "측정 시작", Toast.LENGTH_SHORT).show()
                setMeasureButtonClickable(false)
                setTimer()
            }

            tvHistoryMeasureStop.setOnClickListener {
                Toast.makeText(it.context, "측정 중지", Toast.LENGTH_SHORT).show()
                setMeasureButtonClickable(true)
                sensorHistoryMeasureViewModel.timerStop()
            }

            btnHistoryAccMeasure.setOnClickListener {
                setSensorListener(Sensor.TYPE_ACCELEROMETER)
                Toast.makeText(it.context, "ACC", Toast.LENGTH_SHORT).show()
            }

            btnHistoryGyroMeasure.setOnClickListener {
                setSensorListener(Sensor.TYPE_GYROSCOPE)
                Toast.makeText(it.context, "GYRO", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun setMeasureButtonClickable(state: Boolean) {
        binding.apply {
            btnHistoryAccMeasure.isClickable = state
            btnHistoryGyroMeasure.isClickable = state
        }
    }

    private fun setSensorListener(sensorType: Int) {
        sensorManager.unregisterListener(sensorEventListener)
        sensor = sensorManager.getDefaultSensor(sensorType)
        registerSensorListener(sensorEventListener, sensor)
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

        binding.tvHistoryMeasureX.text = format.format(event.values[0]).toString() //x축
        binding.tvHistoryMeasureY.text = format.format(event.values[1]).toString() //y축
        binding.tvHistoryMeasureZ.text = format.format(event.values[2]).toString() //z축

        xList.add(format.format(event.values[0]).toFloat())
        yList.add(format.format(event.values[1]).toFloat())
        zList.add(format.format(event.values[2]).toFloat())
    }

}