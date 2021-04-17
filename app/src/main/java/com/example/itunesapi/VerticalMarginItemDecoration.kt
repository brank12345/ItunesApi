package com.example.itunesapi

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalMarginItemDecoration(
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter ?: return
        val count = adapter.itemCount.takeIf { it > 0 } ?: return

        when (position) {
            0 -> {
                // First item
                outRect[left, top, right] = bottom / 2
            }
            count - 1 -> {
                // Last item
                outRect[left, top / 2, right] = bottom
            }
            else -> {
                outRect[left, top / 2, right] = bottom / 2
            }
        }
    }
}