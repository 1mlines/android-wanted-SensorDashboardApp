package com.preonboarding.sensordashboard.presentation.viewmodel

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
import com.preonboarding.sensordashboard.domain.model.MeasureValue
import com.preonboarding.sensordashboard.domain.usecase.SaveSensorHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SensorHistoryMeasureViewModel @Inject constructor(
    private val saveSensorHistoryUseCase: SaveSensorHistoryUseCase,
) : BaseViewModel() {
    private var isRun: Boolean = false
    private lateinit var job: Job
    private var interval: Long = 100

    private val _currentMeasureValue = MutableStateFlow(MeasureValue())
    val currentMeasureValue = _currentMeasureValue.asStateFlow()

    var lineData: LineData = LineData()
    private val lineDataSet = ArrayList<ILineDataSet>()
    private lateinit var xEntries: ArrayList<Entry>
    private lateinit var yEntries: ArrayList<Entry>
    private lateinit var zEntries: ArrayList<Entry>

    fun initLineData() {
        xEntries = arrayListOf(Entry(0f, 0f))
        yEntries = arrayListOf(Entry(0f, 0f))
        zEntries = arrayListOf(Entry(0f, 0f))
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
        // TODO 데이터세트 세팅 필요
        // 정호님 화이팅
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

    fun updateCurrentMeasureValue(x: Float, y: Float, z: Float) {
        viewModelScope.launch {
            _currentMeasureValue.update {
                _currentMeasureValue.value.copy(x, y, z)
            }
        }
    }

    // 선택된 히스토리 데이터 초기화용
    fun setLineData(){
        initLineData()
        //TODO Item 넘겨받아 뷰모델 lineData 세팅 필요
    }

    fun timerStart(fnCallback: () -> Unit) {
        isRun = true
        if (::job.isInitialized) job.cancel()

        job = viewModelScope.launch {
            while (true) {
                delay(interval)
                fnCallback()
            }
        }
    }

    fun timerStop() {
        isRun = false
        if (::job.isInitialized) job.cancel()
    }

    fun saveSensorHistory() {
//        saveSensorHistoryUseCase()
    }
}