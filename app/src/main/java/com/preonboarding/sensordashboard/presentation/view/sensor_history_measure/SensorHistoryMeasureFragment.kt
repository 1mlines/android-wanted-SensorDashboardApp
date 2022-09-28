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

@AndroidEntryPoint
class SensorHistoryMeasureFragment :
    BaseFragment<FragmentSensorHistoryMeasureBinding>(R.layout.fragment_sensor_history_measure),
    SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var sensor: Sensor? = null
    private val sensorHistoryMeasureViewModel: SensorHistoryMeasureViewModel by viewModels()
    private val format = DecimalFormat("#.##")
    private lateinit var history:SensorHistory
    var xList = mutableListOf<Float>()
    var yList = mutableListOf<Float>()
    var zList = mutableListOf<Float>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager


        binding.historyMeasureTextView.setOnClickListener {
            Toast.makeText(it.context, "측정 시작", Toast.LENGTH_SHORT).show()
            binding.historyMeasureAccButton.isClickable = false
            binding.historyMeasureGyroButton.isClickable = false
            setTimer()
            drawGraph()
        }

        binding.historyMeasureStopTextView.setOnClickListener {
            Toast.makeText(it.context, "측정 중지", Toast.LENGTH_SHORT).show()
            binding.historyMeasureAccButton.isClickable = true
            binding.historyMeasureGyroButton.isClickable = true
            sensorHistoryMeasureViewModel.timerStop()
            sensorManager.unregisterListener(this)

        }

        binding.historyMeasureAccButton.setOnClickListener {
            sensorManager.unregisterListener(this)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
            Toast.makeText(it.context, "ACC", Toast.LENGTH_SHORT).show()
            sensorManager.apply {
                registerListener(
                    this@SensorHistoryMeasureFragment,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        }

        binding.historyMeasureGyroButton.setOnClickListener {
            sensorManager.unregisterListener(this)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)
            Toast.makeText(it.context, "GYRO", Toast.LENGTH_SHORT).show()
            sensorManager.apply {
                registerListener(
                    this@SensorHistoryMeasureFragment,
                    sensor,
                    SensorManager.SENSOR_DELAY_NORMAL
                )
            }
        }


    }

    private fun setTimer() {
        sensorHistoryMeasureViewModel.setInterval(100)
        var ss = 0
        var ms = 0
        val timerAction: () -> Unit = {
            if (ms==10){
                ms = 0
                ss++
            }
            binding.historyMeasureTimerTextView.text = "${ss}:${ms++}"
            Timber.d("현재 값 : ${xList[xList.lastIndex]},${yList[yList.lastIndex]},${zList[zList.lastIndex]}")
            //히스토리에 값 집어넣기

        }
        sensorHistoryMeasureViewModel.timerStart(timerAction)
    }


    override fun onResume() {
        super.onResume()
        sensorManager.apply {
            registerListener(this@SensorHistoryMeasureFragment, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (event != null) {
            when (event.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> getSensorData(event)
                Sensor.TYPE_GYROSCOPE -> getSensorData(event)
            }
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) = Unit

    private fun getSensorData(event: SensorEvent) {
        binding.historyXMeasureTextView.text = format.format(event.values[0]).toString() //x축
        binding.historyYMeasureTextView.text = format.format(event.values[1]).toString() //y축
        binding.historyZMeasureTextView.text = format.format(event.values[2]).toString() //z축

        xList.add(format.format(event.values[0]).toFloat())
        yList.add(format.format(event.values[1]).toFloat())
        zList.add(format.format(event.values[2]).toFloat())
    }

    private fun drawGraph() {
        //그래프 그리기

    }

    private fun saveData(history:SensorHistory) {
        //데이터 저장
        //히스토리 초기화
    }


}