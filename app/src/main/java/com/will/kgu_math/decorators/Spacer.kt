package com.will.kgu_math.decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class Spacer(
    private val left: Int,
    private val top: Int,
    private val right: Int,
    private val bottom: Int
) : RecyclerView.ItemDecoration() {

    constructor(rl: Int, tb: Int) : this(rl, tb, rl, tb)

    constructor(all: Int): this(all, all, all, all)

    override fun getItemOffsets(
        rect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildAdapterPosition(view) == 0)
            rect.top = top
        rect.left = left
        rect.right = right
        rect.bottom = bottom
    }
}