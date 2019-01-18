package com.github.lekaha.views.scrollhandler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

interface ScrollHandler {

    fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler): Int

    interface Callback {
        fun getChildCount(): Int
        fun getChildAt(index: Int): View?
        fun getHeight(): Int
        fun getBottom(): Int
        fun getRevealHeight(): Int
        fun getFirstVisiblePosition(): Int
        fun getPaddingTop(): Int
        fun getLastVisiblePosition(): Int
        fun getItemCount(): Int
        fun removeView(view: View)
        fun addView(view: View)
        fun addView(newFirstView: View, position: Int)
    }
}