package com.preonboarding.sensordashboard.presentation.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.preonboarding.sensordashboard.R
import com.preonboarding.sensordashboard.databinding.ActivityMainBinding
import com.preonboarding.sensordashboard.presentation.view.sensor_history_measure.SensorHistoryMeasureFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}