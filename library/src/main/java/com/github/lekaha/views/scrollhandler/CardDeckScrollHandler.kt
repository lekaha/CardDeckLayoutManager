package com.github.lekaha.views.scrollhandler

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.min

class CardDeckScrollHandler(
    private val callback: ScrollHandler.Callback,
    private val debug: Boolean = true
) : ScrollHandler {

    private var lastVisibleViewScrolled: Int = 0
    private var firstVisibleViewScrolled: Int = 0

    override fun scrollVerticallyBy(
        dy: Int,
        recycler: RecyclerView.Recycler
    ): Int {
        if (callback.getChildCount() == 0 || dy == 0) {
            return 0
        }

        val revealHeight = callback.getRevealHeight()
        val totalScrollable =
            (callback.getChildCount() - 1) * revealHeight +
                    (callback.getChildAt(0)?.measuredHeight ?: 0) -
                    callback.getHeight()
        var position = callback.getChildCount() -
                (callback.getChildCount() * revealHeight - lastVisibleViewScrolled ) / revealHeight
        var delta = min(abs(dy), totalScrollable) * (dy / abs(dy))
        if (delta > 0) {
            val gap = callback.getChildAt(position)?.top ?: 0 - (callback.getChildAt(position + 1)?.top ?: 0)
            val remainScrollable = (totalScrollable - lastVisibleViewScrolled).let {
                if (it > 0) it else 0
            }
            delta = min(delta, remainScrollable)
            lastVisibleViewScrolled += delta
            firstVisibleViewScrolled = (totalScrollable - lastVisibleViewScrolled).let {
                if (it > 0) it else 0
            }

            if (gap > delta) {
                scrollAllViewBy(position, -delta)
                if (debug) {
                    Log.d(
                        "Card", "[up] Position=" + position + " delta=" + delta +
                                " firstVisibleViewScrolled=" + firstVisibleViewScrolled +
                                " lastVisibleViewScrolled=" + lastVisibleViewScrolled
                    )
                }
                return delta
            } else {
                scrollAllViewBy(position, -gap)
                delta -= gap
                position += 1

                if (position == 0) return delta
            }

            while ((delta - revealHeight) > 0) {
                scrollAllViewBy(position, revealHeight)
                delta -= revealHeight
                position += 1

                if (position == 0) return delta
            }

            scrollAllViewBy(position, -delta)

            if (debug) {
                Log.d(
                    "Card", "[up] Position=" + position + " delta=" + delta +
                            " firstVisibleViewScrolled=" + firstVisibleViewScrolled +
                            " lastVisibleViewScrolled=" + lastVisibleViewScrolled
                )
            }

        } else {
            val gap = callback.getChildAt(position)?.top ?: 0 - (callback.getChildAt(position - 1)?.top ?: 0)
            val remainScrollable = (totalScrollable - firstVisibleViewScrolled).let {
                if (it > 0) it else 0
            }
            delta = min(abs(delta), remainScrollable)
            firstVisibleViewScrolled += delta
            delta = -delta

            lastVisibleViewScrolled = (totalScrollable - firstVisibleViewScrolled).let {
                if (it > 0) it else 0
            }

            if ((revealHeight - gap) > abs(delta)) {
                scrollAllViewBy(position, -delta)
                if (debug) {
                    Log.d(
                        "Card", "[down] Position=" + position + " delta=" + delta +
                                " firstVisibleViewScrolled=" + firstVisibleViewScrolled +
                                " lastVisibleViewScrolled=" + lastVisibleViewScrolled
                    )
                }
                return delta
            } else {
                scrollAllViewBy(position, (revealHeight - gap))
                delta += (revealHeight - gap)
                position -= 1

                if (position == 0) return delta
            }

            // In case of delta is too much
            while ((abs(delta) - revealHeight) > 0) {
                scrollAllViewBy(position, revealHeight)
                delta += revealHeight
                position -= 1

                if (position == 0) return delta
            }

            scrollAllViewBy(position, -delta)

            if (debug) {
                Log.d(
                    "Card", "[down] Position=" + position + " delta=" + delta +
                            " firstVisibleViewScrolled=" + firstVisibleViewScrolled +
                            " lastVisibleViewScrolled=" + lastVisibleViewScrolled
                )
            }
        }
        return delta
    }

    /**
     * This method tell all views should scroll down with the offset
     */
    private fun scrollAllViewBy(position: Int, indexOffset: Int) {
        if (position == 0 && (position == callback.getChildCount() - 1)) {
            return
        }

        for (i in position..callback.getChildCount()) {
            callback.getChildAt(i)?.also { view ->
                    scrollSingleViewVerticallyBy(view, indexOffset)
            }
        }
    }

    /**
     * This method calculates new position of single view
     */
    private fun scrollSingleViewVerticallyBy(view: View, indexOffset: Int) {
        view.offsetTopAndBottom(if ((view.top + indexOffset) < 0) -view.top else indexOffset)
    }

}