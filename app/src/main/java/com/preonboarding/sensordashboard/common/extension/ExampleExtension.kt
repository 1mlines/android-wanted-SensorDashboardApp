package com.preonboarding.sensordashboard.common.extension

import androidx.appcompat.widget.AppCompatButton
import com.preonboarding.sensordashboard.R

fun AppCompatButton.setSelectedColor(state: Boolean) {
    if (state) {
        setBackgroundResource(R.drawable.bg_button_selected)
    } else {
        setBackgroundResource(R.drawable.bg_button_unselected)
    }
}