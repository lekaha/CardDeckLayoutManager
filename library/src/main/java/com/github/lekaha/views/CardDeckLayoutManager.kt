package com.github.lekaha.views

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.github.lekaha.views.scrollhandler.CardDeckScrollHandler
import com.github.lekaha.views.scrollhandler.ScrollHandler

class CardDeckLayoutManager(
    val context: Context,
    recyclerView: RecyclerView,
    private val revealPixelHeight: Int
) : RecyclerView.LayoutManager(), ScrollHandler.Callback {

    companion object {
        const val FIRST_VISIBLE_POSITION = "FIRST_VISIBLE_POSITION"
        const val LAST_VISIBLE_POSITION = "LAST_VISIBLE_POSITION"
    }

    private var firstVisiblePosition = 0
    private var lastVisiblePosition = 0

    /* Used for tracking off-screen change events */
    private var firstChangedPosition: Int = 0
    private var changedPositionCount: Int = 0

    /* Consistent size applied to all child views */
    private var decoratedChildWidth: Int = 0
    private var decoratedChildHeight: Int = 0

    private var previousViewCenter: Point? = null
    private var recyclerViewBottom: Int = 0

    private val scrollHandler: ScrollHandler = CardDeckScrollHandler(this)

    init {
        recyclerView.setHasFixedSize(true)
        recyclerViewBottom = recyclerView.bottom
    }

    override fun getFirstVisiblePosition() = firstVisiblePosition

    override fun getLastVisiblePosition() = lastVisiblePosition

    override fun getRevealHeight() = revealPixelHeight

    override fun getBottom() = recyclerViewBottom

    override fun supportsPredictiveItemAnimations() = true

    override fun canScrollHorizontally() = false

    override fun canScrollVertically() = true

    override fun onItemsRemoved(recyclerView: RecyclerView, positionStart: Int, itemCount: Int) {
        firstChangedPosition = positionStart
        changedPositionCount = itemCount
    }

    override fun generateDefaultLayoutParams(): LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )

    override fun onSaveInstanceState(): Parcelable {
        return bundleOf(
            FIRST_VISIBLE_POSITION to firstVisiblePosition,
            LAST_VISIBLE_POSITION to lastVisiblePosition
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state?.let {
            if (it is Bundle) {
                firstVisiblePosition = it.getInt(FIRST_VISIBLE_POSITION)
                lastVisiblePosition = it.getInt(LAST_VISIBLE_POSITION)
            }
        }
    }

    override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler, state: RecyclerView.State): Int {
        if (childCount == 0) {
            return 0
        }

        return scrollHandler.scrollVerticallyBy(dy, recycler)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {

        //We have nothing to show for an empty data set but clear any existing views
        if (itemCount == 0) {
            detachAndScrapAttachedViews(recycler)
            return
        }

        if (childCount == 0 && state.isPreLayout) {
            //Nothing to do during prelayout when empty
            return
        }

        // TODO: These values should not be set to "0". They should be restored from state
        firstVisiblePosition = 1
        lastVisiblePosition = 0

        ///Clear change tracking state when a real layout occurs
        if (!state.isPreLayout) {
            changedPositionCount = 0
            firstChangedPosition = 0
        }

        if (childCount == 0) { //First or empty layout
            for (i in 0 until itemCount) {
                //Scrap measure one child
                val scrap = recycler.getViewForPosition(i)
                detachView(scrap)
            }
        }

//        var isLastLayoutView: Boolean
        do {
            val view = recycler.getViewForPosition(lastVisiblePosition)
            measureChildWithMargins(view, 0, 0)
            addView(view)
            layoutNextView(view)
//            isLastLayoutView = isLastLayoutedView(height, view)
            lastVisiblePosition++
        } while (lastVisiblePosition < itemCount)
    }

    private fun performLayout(view: View, viewCenter: Point, halfViewWidth: Int, halfViewHeight: Int) {
        val top = viewCenter.y - halfViewHeight
        val bottom = viewCenter.y + halfViewHeight

        val left = viewCenter.x - halfViewWidth
        val right = viewCenter.x + halfViewWidth

        layoutDecorated(view, left, top, right, bottom)
    }

    fun layoutNextView(view: View) {
        decoratedChildWidth = width
        decoratedChildHeight = getDecoratedMeasuredHeight(view)

        val viewCenter =
            previousViewCenter?.let {
                findNextViewCenter(it, decoratedChildWidth / 2, decoratedChildHeight / 2)
            } ?: run {
                previousViewCenter = Point(
                    paddingLeft + decoratedChildWidth / 2,
                    paddingTop + decoratedChildHeight / 2
                )
                previousViewCenter!!
            }

        performLayout(view, viewCenter, decoratedChildWidth / 2, decoratedChildHeight / 2)
    }

    fun findNextViewCenter(center: Point, halfWidth: Int, halfHeight: Int): Point {
        previousViewCenter = Point(
            paddingLeft + halfWidth,
            center.y + revealPixelHeight
        )
        return previousViewCenter!!
    }

    /**
     * This method checks if this is last visible layouted view.
     * The return might be used to know if we should stop laying out
     */
    fun isLastLayoutedView(recyclerHeight: Int, view: View): Boolean {
        return (view.top + revealPixelHeight) >= recyclerHeight
    }
}