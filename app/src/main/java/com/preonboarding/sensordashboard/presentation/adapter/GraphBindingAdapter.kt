package com.preonboarding.sensordashboard.presentation.adapter

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("bindValueX")
fun TextView.bindValueX(value: Float) {
    val formatData = String.format("%.4f", value)
    text = "X: $formatData"
}

@BindingAdapter("bindValueY")
fun TextView.bindValueY(value: Float) {
    val formatData = String.format("%.4f", value)
    text = "Y: $formatData"
}

@BindingAdapter("bindValueZ")
fun TextView.bindValueZ(value: Float) {
    val formatData = String.format("%.4f", value)
    text = "Z: $formatData"
}