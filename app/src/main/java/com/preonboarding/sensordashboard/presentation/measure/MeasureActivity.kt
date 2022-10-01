package com.preonboarding.sensordashboard.presentation.measure

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ActivityMeasureBinding
import com.preonboarding.sensordashboard.domain.model.SensorType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MeasureActivity : AppCompatActivity() {
    private val viewModel: MeasureViewModel by viewModels()
    private val binding by lazy { ActivityMeasureBinding.inflate(layoutInflater) }
    private lateinit var time: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initListener()
        initChart()
        observeSensor()
    }

    private fun initListener() = with(binding) {
        var type = SensorType.ACC
        radioGroup.check(R.id.radio_button_acc)
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.radio_button_acc -> {
                    type = SensorType.ACC
                    refreshData()
                }
                R.id.radio_button_gyro -> {
                    type = SensorType.GYRO
                    refreshData()
                }
            }
        }
        tvMeasure.setOnClickListener {
            radioGroup[0].isEnabled = false
            radioGroup[1].isEnabled = false

            initChart()
            viewModel.clearSensorDataList()
            time = object : CountDownTimer(60000, 100) {
                override fun onTick(tick: Long) {
                }

                override fun onFinish() {
                    viewModel.pressStop()
                    radioGroup[0].isEnabled = true
                    radioGroup[1].isEnabled = true
                }
            }.start()
            tvStop.visibility = 0
            when (type) {
                SensorType.ACC -> {
                    viewModel.updateCurrentSensorType(SensorType.ACC)
                    viewModel.measureAccSensor()
                }
                SensorType.GYRO -> {
                    viewModel.updateCurrentSensorType(SensorType.GYRO)
                    viewModel.measureGyroSensor()
                }
                else -> {}
            }
        }
        tvStop.setOnClickListener {
            time.onFinish()
            time.cancel()

            radioGroup[0].isEnabled = true
            radioGroup[1].isEnabled = true
            tvStop.visibility = 4
        }

        tvSave.setOnClickListener {
            println(viewModel.sensorDataList)
            if (viewModel.sensorDataList.size == 1 || viewModel.sensorDataList.isEmpty()) {
                Toast.makeText(this@MeasureActivity, R.string.measure_empty, Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.saveSensorData()
                Toast.makeText(
                    this@MeasureActivity,
                    R.string.measure_save_success,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        ivBack.setOnClickListener {
            finish()
        }
    }

    private fun observeSensor() = with(lifecycleScope) {
        launch {
            repeatOnLifecycle(state = Lifecycle.State.RESUMED) {
                with(viewModel) {
                    measuredSensorData.collect { sensorAxisData ->
                        addSensorAxisData(sensorAxisData)
                        addEntry(sensorAxisData.x.toDouble(), label = "x")
                        addEntry(sensorAxisData.y.toDouble(), label = "y")
                        addEntry(sensorAxisData.z.toDouble(), label = "z")
                        binding.tvX.text = "x ${sensorAxisData.x.toDouble()}"
                        binding.tvY.text = "y ${sensorAxisData.y.toDouble()}"
                        binding.tvZ.text = "z ${sensorAxisData.z.toDouble()}"
                    }
                }
            }
        }
    }

    private fun initChart() = with(binding.lineChart) {
        setDrawGridBackground(true)
        setBackgroundColor(Color.WHITE)
        setGridBackgroundColor(Color.WHITE)
        description.text = ""

        setTouchEnabled(true)
        setScaleEnabled(false)

        isAutoScaleMinMaxEnabled = true
        setPinchZoom(false)

        xAxis.setDrawGridLines(true)
        xAxis.setDrawAxisLine(false)
        xAxis.isEnabled = true

        axisLeft.isEnabled = true
        axisLeft.setDrawGridLines(true)
        axisRight.isEnabled = false

        val entryX = mutableListOf<Entry>()
        val entryY = mutableListOf<Entry>()
        val entryZ = mutableListOf<Entry>()

        val xCharDataSet = createSet(color = Color.RED, label = "x", entryX)
        val yChartDataSet = createSet(color = Color.GREEN, label = "y", entryY)
        val zChartDataSet = createSet(color = Color.BLUE, label = "z", entryZ)

        val dataSets = arrayListOf<ILineDataSet>()
        dataSets.add(xCharDataSet)
        dataSets.add(yChartDataSet)
        dataSets.add(zChartDataSet)
        val chartData = LineData(dataSets)

        data = chartData

        invalidate()
    }

    private fun createSet(color: Int, label: String, entry: MutableList<Entry>): LineDataSet {
        val set = LineDataSet(entry, label)
        set.lineWidth = 1f
        set.setDrawValues(false)
        set.valueTextColor = Color.WHITE
        set.color = color
        set.mode = LineDataSet.Mode.LINEAR
        set.setDrawCircles(false)

        return set
    }

    private fun addEntry(num: Double, label: String) = with(binding.lineChart) {
        when (label) {
            "x" -> {
                val set = data.getDataSetByLabel(label, false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 0)
            }
            "y" -> {
                val set = data.getDataSetByLabel(label, false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 1)
            }
            "z" -> {
                val set = data.getDataSetByLabel(label, false)
                data.addEntry(Entry(set.entryCount.toFloat(), num.toFloat()), 2)
            }
        }
        data.notifyDataChanged()

        notifyDataSetChanged()
        setVisibleXRangeMaximum(10f)
        moveViewToX(data.entryCount.toFloat())
    }

    private fun refreshData() {
        initChart()
        binding.tvX.setText(R.string.measure_x_init)
        binding.tvY.setText(R.string.measure_y_init)
        binding.tvZ.setText(R.string.measure_z_init)
        viewModel.clearSensorDataList()
    }
}
