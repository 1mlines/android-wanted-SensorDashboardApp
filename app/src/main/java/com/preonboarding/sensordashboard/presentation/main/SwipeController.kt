package com.preonboarding.sensordashboard.presentation.main

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.preonboarding.sensordashboard.R

class SwipeController(
    private val context: Context,
    private val itemTouchHelperListener: ItemTouchHelperListener
) :
    ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.ic_delete_white_24)
    private val deleteIntrinsicWidth = deleteIcon?.intrinsicWidth
    private val deleteIntrinsicHeight = deleteIcon?.intrinsicHeight
    private val playIcon = ContextCompat.getDrawable(context, R.drawable.ic_play_white_24)
    private val playIntrinsicWidth = playIcon?.intrinsicWidth
    private val playIntrinsicHeight = playIcon?.intrinsicHeight
    private val background = ColorDrawable()
    private val deleteBackgroundColor = ContextCompat.getColor(context, R.color.red)
    private val playBackgroundColor = ContextCompat.getColor(context, R.color.green)
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        when (direction) {
            ItemTouchHelper.LEFT -> {
                itemTouchHelperListener.onRightView(viewHolder.bindingAdapterPosition, viewHolder)
            }
            ItemTouchHelper.RIGHT -> {
                itemTouchHelperListener.onLeftView(viewHolder.bindingAdapterPosition, viewHolder)
            }
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val itemHeight = itemView.bottom - itemView.top
        val isCanceled = dX == 0f && !isCurrentlyActive

        if (isCanceled) {
            clearCanvas(
                c,
                itemView.right + dX,
                itemView.top.toFloat(),
                itemView.right.toFloat(),
                itemView.bottom.toFloat()
            )
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        if (dX > 0) {
            background.color = deleteBackgroundColor
            background.setBounds(
                itemView.left,
                itemView.top,
                itemView.left + dX.toInt(),
                itemView.bottom
            )
            background.draw(c)

            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - (deleteIntrinsicHeight ?: 0)) / 2
            val deleteIconMargin = (itemHeight - (deleteIntrinsicHeight ?: 0)) / 2
            val deleteIconLeft = itemView.left + deleteIconMargin
            val deleteIconRight = itemView.left + deleteIconMargin + (deleteIntrinsicWidth ?: 0)
            val deleteIconBottom = deleteIconTop + (deleteIntrinsicHeight ?: 0)

            // Draw the delete icon
            if (deleteIcon != null) {
                deleteIcon.setBounds(
                    deleteIconLeft,
                    deleteIconTop,
                    deleteIconRight,
                    deleteIconBottom
                )
                deleteIcon.draw(c)
            }
        } else if (dX < 0) {
            // Draw the blue play background
            background.color = playBackgroundColor
            background.setBounds(
                itemView.right + dX.toInt(),
                itemView.top,
                itemView.right,
                itemView.bottom
            )
            background.draw(c)

            // Calculate position of play icon
            val playIconTop = itemView.top + (itemHeight - (playIntrinsicHeight ?: 0)) / 2
            val playIconMargin = (itemHeight - (playIntrinsicHeight ?: 0)) / 2
            val playIconLeft = itemView.right - playIconMargin - (playIntrinsicWidth ?: 0)
            val playIconRight = itemView.right - playIconMargin
            val playIconBottom = playIconTop + (playIntrinsicHeight ?: 0)

            // Draw the play icon
            if (playIcon != null) {
                playIcon.setBounds(playIconLeft, playIconTop, playIconRight, playIconBottom)
                playIcon.draw(c)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}
