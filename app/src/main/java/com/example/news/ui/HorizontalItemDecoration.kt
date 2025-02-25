package com.example.news.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(
    private val itemsToShow: Float // Number of items to show at once (e.g., 2.5f)
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        // Calculate the width of each item
        val itemWidth = (parent.width / itemsToShow).toInt()

        // Set the width of the item
        view.layoutParams.width = itemWidth
        view.requestLayout()
    }
}