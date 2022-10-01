package com.preonboarding.sensordashboard.presentation.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ActivityMainBinding
import com.preonboarding.sensordashboard.domain.model.SensorData
import com.preonboarding.sensordashboard.presentation.measure.MeasureActivity
import com.preonboarding.sensordashboard.presentation.replay.ReplayActivity
import com.preonboarding.sensordashboard.util.GraphType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val sensorViewModel: SensorViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var recyclerViewAdapter: MainRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        setSupportActionBar(binding.toolbarMain)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding.viewModel = sensorViewModel
        binding.lifecycleOwner = this

        binding.recyclerviewMain.apply {
            recyclerViewAdapter = MainRecyclerViewAdapter(sensorViewModel)
            adapter = recyclerViewAdapter
        }

        val swipeController = SwipeController(applicationContext, recyclerViewAdapter)
        ItemTouchHelper(swipeController).attachToRecyclerView(binding.recyclerviewMain)

        recyclerViewAdapter.setOnSwipeClickListener(object :
            MainRecyclerViewAdapter.OnSwipeClickListener {
                override fun onLeftView(sensorData: SensorData) {
                    sensorViewModel.deleteSensorData(sensorData.id)
                }

                override fun onRightView(sensorData: SensorData) {
                    // 화면전환 코드 추가 필요
                    val data = Json.encodeToString(sensorData)
                    val intent = Intent(this@MainActivity, ReplayActivity::class.java)
                    intent.putExtra("sensorData", data)
                    intent.putExtra("type", GraphType.PLAY)
                    startActivity(intent)
                }
            }
        )

        recyclerViewAdapter.setOnClickListener(object :
            MainRecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(sensorData: SensorData) {
                val data = Json.encodeToString(sensorData)
                val intent = Intent(this@MainActivity, ReplayActivity::class.java)
                intent.putExtra("sensorData", data)
                intent.putExtra("type", GraphType.VIEW)
                startActivity(intent)
            }
        })

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sensorViewModel.sensorsDataPagingFlow.collectLatest { sensors ->
                    recyclerViewAdapter.submitData(sensors)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.item_main_measure -> {
                startActivity(Intent(applicationContext, MeasureActivity::class.java))
                super.onOptionsItemSelected(item)
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
