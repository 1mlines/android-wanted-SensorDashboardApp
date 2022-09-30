package com.preonboarding.sensordashboard.presentation.view.sensor_history_play

import android.graphics.Color
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.preonboarding.sensordashboard.common.base.BaseViewModel
import com.preonboarding.sensordashboard.common.constant.ViewName
import com.preonboarding.sensordashboard.domain.model.SensorHistory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorHistoryPlayViewModel @Inject constructor(
) : BaseViewModel() {
    var isPlay: Boolean = false

    private lateinit var currentHistory: SensorHistory

    private lateinit var playJob: Job
    private lateinit var timerJob: Job
    private var interval: Long = 100

    private lateinit var xValues: ArrayList<Entry>
    private lateinit var yValues: ArrayList<Entry>
    private lateinit var zValues: ArrayList<Entry>

    var timerSs = 0
    var timerMs = 0

    private var drawIndex = 0

    var lineData: LineData = LineData()
    private val lineDataSet = ArrayList<ILineDataSet>()
    private lateinit var xEntries: ArrayList<Entry>
    private lateinit var yEntries: ArrayList<Entry>
    private lateinit var zEntries: ArrayList<Entry>

    fun initLineData(history: SensorHistory, viewName: ViewName) {
        currentHistory = history

        xEntries = arrayListOf(Entry(0f, 0f))
        yEntries = arrayListOf(Entry(0f, 0f))
        zEntries = arrayListOf(Entry(0f, 0f))

        xValues = floatToEntry(currentHistory.xList)
        yValues = floatToEntry(currentHistory.yList)
        zValues = floatToEntry(currentHistory.zList)

        lineDataSet.apply {
            when (viewName) {
                ViewName.SHOW -> {
                    add(getLineDataSet(xValues, "x", Color.RED))
                    add(getLineDataSet(yValues, "y", Color.GREEN))
                    add(getLineDataSet(zValues, "z", Color.BLUE))
                }
                else -> {
                    add(getLineDataSet(xEntries, "x", Color.RED))
                    add(getLineDataSet(yEntries, "y", Color.GREEN))
                    add(getLineDataSet(zEntries, "z", Color.BLUE))
                }
            }
        }
        lineData = LineData(lineDataSet)
    }

    private fun getLineDataSet(
        entries: ArrayList<Entry>,
        type: String,
        colorType: Int
    ): LineDataSet {
        return LineDataSet(entries, type).apply {
            color = colorType
            setCircleColor(colorType)
            circleHoleColor = colorType
            lineWidth = 1f
            circleRadius = 1f
            isHighlightEnabled = false
            mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }
    }

    fun playStart(callBack: (Int) -> Unit, endCallBack: () -> Unit) {
        isPlay = true
        if (::playJob.isInitialized) playJob.cancel()

        playJob = viewModelScope.launch {
            for (i in drawIndex until xValues.size) {
                delay(50)
                drawIndex = i
                lineData.addEntry(xValues[i], 0)
                lineData.addEntry(yValues[i], 1)
                lineData.addEntry(zValues[i], 2)
                lineData.notifyDataChanged()
                callBack(drawIndex)

                if (i == xValues.lastIndex) {
                    endCallBack()
                }
            }
        }
    }

    fun playStop() {
        isPlay = false

        if (::playJob.isInitialized) playJob.cancel()
    }

    private fun floatToEntry(values: List<Float>): ArrayList<Entry> {
        val result = ArrayList<Entry>()

        for (i in values.indices) {
            result.add(Entry(i.toFloat(), values[i]))
        }

        return result
    }

    fun timerStart(fnCallback: () -> Pair<Int, Int>) {
        if (::timerJob.isInitialized) timerJob.cancel()

        timerJob = viewModelScope.launch {
            while (true) {
                delay(interval)
                timerSs = fnCallback().first
                timerMs = fnCallback().second
            }
        }
    }

    fun timerStop() {
        if (::timerJob.isInitialized) timerJob.cancel()
    }
}
