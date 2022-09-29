package com.preonboarding.sensordashboard.presentation.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("bindValue")
fun TextView.bindValue(value: Float) {
    text = value.toString()
}