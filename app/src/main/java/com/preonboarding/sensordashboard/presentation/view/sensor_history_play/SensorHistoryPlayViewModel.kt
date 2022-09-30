package com.preonboarding.sensordashboard.presentation.view.sensor_history_play

import android.graphics.Color
import androidx.lifecycle.viewModelScope
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.preonboarding.sensordashboard.common.base.BaseViewModel
import com.preonboarding.sensordashboard.common.constant.Constants.X
import com.preonboarding.sensordashboard.common.constant.Constants.Y
import com.preonboarding.sensordashboard.common.constant.Constants.Z
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorHistoryPlayViewModel @Inject constructor(
) : BaseViewModel() {
    var isPlay: Boolean = false

    private lateinit var playJob: Job
    private lateinit var timerJob: Job
    private var interval: Long = 100

    // TODO : 테스트 값
    private val xValues = ArrayList<Entry>()
    private val yValues = ArrayList<Entry>()
    private val zValues = ArrayList<Entry>()

    var timerSs = 0
    var timerMs = 0

    private var drawIndex = 0

    var lineData: LineData = LineData()
    private val lineDataSet = ArrayList<ILineDataSet>()
    private lateinit var xEntries: ArrayList<Entry>
    private lateinit var yEntries: ArrayList<Entry>
    private lateinit var zEntries: ArrayList<Entry>

    init {
        setValues(xValues)
        setValues(yValues)
        setValues(zValues)
        initLineData()
    }

    private fun initLineData() {

        // IF문 여기서 돌려야할듯 (PLAY? VIEW?)


        xEntries = arrayListOf(Entry(0f, 0f))
        yEntries = arrayListOf(Entry(0f, 0f))
        zEntries = arrayListOf(Entry(0f, 0f))

        // VIEW
//        lineDataSet.apply {
//            add(getLineDataSet(xValues, "x", Color.RED))
//            add(getLineDataSet(yValues, "y", Color.GREEN))
//            add(getLineDataSet(zValues, "z", Color.BLUE))
//        }


        // PLAY
        lineDataSet.apply {
            add(getLineDataSet(xEntries, "x", Color.RED))
            add(getLineDataSet(yEntries, "y", Color.GREEN))
            add(getLineDataSet(zEntries, "z", Color.BLUE))
        }
        lineData = LineData(lineDataSet)
    }

    fun addEntry(x: Float, y: Float, z: Float) {
        lineData.apply {
            dataSets[X].addEntry(Entry(dataSets[X].entryCount.toFloat(), x))
            dataSets[Y].addEntry(Entry(dataSets[Y].entryCount.toFloat(), y))
            dataSets[Z].addEntry(Entry(dataSets[Z].entryCount.toFloat(), z))
            notifyDataChanged()
        }
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
            // TODO : 실제 데이터 넣기...
            for (i in drawIndex until xValues.size) {
                delay(100)
                drawIndex = i
                lineData.addEntry(xValues[i], 0)
                lineData.addEntry(yValues[i], 1)
                lineData.addEntry(zValues[i], 2)
                lineData.notifyDataChanged()
                callBack(drawIndex)

                if(i == xValues.lastIndex) {
                    endCallBack()
                }
            }
        }
    }

    fun playStop() {
        isPlay = false

        if (::playJob.isInitialized) playJob.cancel()
    }

    private fun setValues(values: ArrayList<Entry>) {
        for (i in 0 until 600) {
            val value = (Math.random() * 10).toFloat()
            values.add(Entry(i.toFloat(), value))
        }
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
