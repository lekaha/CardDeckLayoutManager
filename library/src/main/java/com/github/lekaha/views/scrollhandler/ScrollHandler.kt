package com.github.lekaha.views.scrollhandler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface ScrollHandler {

    fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, firstIndexOfItem: Int): Int

    interface Callback {
        fun getChildCount(): Int
        fun getChildAt(index: Int): View?
        fun getHeight(): Int
        fun getFirstVisiblePosition(): Int
        fun getDecoratedTop(view: View): Int
        fun getPaddingTop(): Int
        fun getLastVisiblePosition(): Int
        fun getItemCount(): Int
        fun removeView(view: View)
        fun incrementFirstVisiblePosition()
        fun incrementLastVisiblePosition()
        fun addView(view: View)
        fun decrementLastVisiblePosition()
        fun decrementFirstVisiblePosition()
        fun addView(newFirstView: View, position: Int)
    }
}