package com.github.lekaha.views.scrollhandler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CardDeckScrollHandler(val callback: ScrollHandler.Callback) : ScrollHandler {

    var lastVisibleViewScrolled: Int = 0

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler
    ): Int {
        var delta = dy
        if (delta > 0) {
            callback.getChildAt(callback.getChildCount() - 1)?.also { view ->
                val parentView = view.parent as View
                if (view.bottom > parentView.bottom) {
                    if ((view.bottom - delta) < callback.getBottom()) {
                        delta = view.bottom - callback.getBottom()
                    }
                } else {
                    delta = 0
                }
            }

            lastVisibleViewScrolled += delta
            if (lastVisibleViewScrolled > callback.getRevealHeight()) {
                delta = lastVisibleViewScrolled - callback.getRevealHeight()
                lastVisibleViewScrolled = 0
                callback.incrementFirstVisiblePosition()
            }
        } else {
            if (callback.getFirstVisiblePosition() == 0) {
                return 0
            }
            if ((lastVisibleViewScrolled + delta) < 0) {
                delta = -lastVisibleViewScrolled
                lastVisibleViewScrolled = callback.getRevealHeight()
                callback.decrementFirstVisiblePosition()
            } else {
                lastVisibleViewScrolled += delta
            }
        }

        scrollAllViewVerticallBy(-delta)
        return delta
    }

    /**
     * This method tell all views should scroll with the offset
     */
    private fun scrollAllViewVerticallBy(indexOffset: Int) {
        for (i in callback.getFirstVisiblePosition() + 1..callback.getChildCount()) {
            callback.getChildAt(i)?.also { view ->
                scrollSingleViewVerticallyBy(view, indexOffset)
            }
        }
    }

    /**
     * This method calculates new position of single view
     */
    private fun scrollSingleViewVerticallyBy(view: View, indexOffset: Int) {
        view.offsetTopAndBottom(if (view.top + indexOffset < 0) -view.top else indexOffset)
    }

}