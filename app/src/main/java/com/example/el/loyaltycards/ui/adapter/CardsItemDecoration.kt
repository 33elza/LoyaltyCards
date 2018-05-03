package com.example.el.loyaltycards.ui.adapter

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View

const val MARGIN = 8f

class CardsItemDecoration(val gridSize: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val gridSpacingPx = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, MARGIN, view.context.resources.getDisplayMetrics()).toInt()

        val itemPosition = (view.getLayoutParams() as RecyclerView.LayoutParams).viewAdapterPosition

        outRect.top = if (itemPosition < gridSize) gridSpacingPx else 0
        outRect.left = if (itemPosition % gridSize == 0) gridSpacingPx else 0
        outRect.right = gridSpacingPx
        outRect.bottom = gridSpacingPx
    }
}