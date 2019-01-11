package com.github.lekaha.views.scrollhandler

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CardDeckScrollHandler(val callback: ScrollHandler.Callback): ScrollHandler {

    override fun scrollVerticallyBy(dy: Int,
                                    recycler: RecyclerView.Recycler,
                                    firstIndexOfItem: Int
    ): Int {
        val d = -dy
        for (i in firstIndexOfItem..callback.getChildCount()) {
            callback.getChildAt(i)?.also {
                scrollSingleViewVerticallyBy(it, d)
            }
        }

        return d
    }

    /**
     * This method calculates new position of single view and returns new center point of first view
     */
    protected fun scrollSingleViewVerticallyBy(view: View, indexOffset: Int) {
        view.offsetTopAndBottom(indexOffset)
    }

}