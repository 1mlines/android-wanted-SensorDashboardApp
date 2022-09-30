package com.preonboarding.sensordashboard.presentation.replay

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.FragmentReplayBinding
import com.preonboarding.sensordashboard.domain.model.SensorInfo
import com.preonboarding.sensordashboard.domain.model.ViewType
import com.preonboarding.sensordashboard.presentation.common.base.BaseFragment
import com.preonboarding.sensordashboard.presentation.common.util.NavigationUtil.navigateUp
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ReplayFragment : BaseFragment<FragmentReplayBinding>(R.layout.fragment_replay) {

    private val viewModel: ReplayViewModel by viewModels()
    private val args: ReplayFragmentArgs by navArgs()
    var sensorInfoList: ArrayList<SensorInfo> = arrayListOf()
    private lateinit var timerJob: Job


    /**
     * @author 이재성
     * args로 ViewType, MeasurementResult 사용하시면 됩니다!!
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerObserver()

        initViews()

        binding.measureResult = args.measureResult
        viewModel.setReplayViewType(args.viewType)
        viewModel.applyTimeFormat(args.measureResult.measureTime)

    }

    private fun registerObserver() {
        binding.viewmodel = viewModel

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.curViewType.collect {
                    binding.viewType = it
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.curPlayType.collect {
                    binding.playType = it
                }
            }
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.timerCount.collect {
                    binding.timerCount = it
                }
            }
        }
    }

    private fun initViews() {
        binding.tbReplay.setNavigationOnClickListener {
            navigateUp()
        }
        if (args.viewType == ViewType.VIEW) {
            viewChart(args.measureResult.measureInfo)
        } else {

            startDrawing()
        }
    }

    fun startDrawing() {
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }
        var realTime = args.measureResult.measureTime
        var i = 0
        timerJob = lifecycleScope.launch {
            while (realTime > 0) {
                sensorInfoList.add(args.measureResult.measureInfo[i])
                viewChart(sensorInfoList)
                realTime -= 0.1
                i++
                delay(100L)
            }

        }
    }

    private fun stopDrawing() {
        if (::timerJob.isInitialized) {
            timerJob.cancel()
        }
    }


    private fun viewChart(InfoList: List<SensorInfo>) {
        val entriesX = ArrayList<Entry>()
        val entriesY = ArrayList<Entry>()
        val entriesZ = ArrayList<Entry>()

        var i = 1F

        for (it in InfoList) {
            entriesX.add(Entry(i, it.x.toFloat()))
            entriesY.add(Entry(i, it.y.toFloat()))
            entriesZ.add(Entry(i, it.z.toFloat()))
            i++
        }

        val dataSetX = LineDataSet(entriesX, "X")
        val dataSetY = LineDataSet(entriesY, "Y")
        val dataSetZ = LineDataSet(entriesZ, "Z")

        dataSetX.color = Color.RED
        dataSetX.setDrawCircles(false)
        dataSetX.setDrawValues(false)

        dataSetY.color = Color.GREEN
        dataSetY.setDrawValues(false)
        dataSetY.setDrawCircles(false)

        dataSetZ.color = Color.BLUE
        dataSetZ.setDrawValues(false)
        dataSetZ.setDrawCircles(false)

        val lineData = LineData()

        lineData.addDataSet(dataSetX)
        lineData.addDataSet(dataSetY)
        lineData.addDataSet(dataSetZ)

        binding.replayLineChart.apply {
            data = lineData
            lineData.notifyDataChanged()
            notifyDataSetChanged()
            invalidate()
        }
    }

}