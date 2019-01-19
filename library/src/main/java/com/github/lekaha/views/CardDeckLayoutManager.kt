package com.github.lekaha.views

import android.content.Context
import android.graphics.Point
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams
import com.github.lekaha.views.scrollhandler.CardDeckScrollHandler
import com.github.lekaha.views.scrollhandler.ScrollHandler

class CardDeckLayoutManager(
    val context: Context,
    recyclerView: RecyclerView,
    private val revealPixelHeight: Int
) : RecyclerView.LayoutManager(), ScrollHandler.Callback {

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

        var i = 0
        do {
            val view = recycler.getViewForPosition(i)
            measureChildWithMargins(view, 0, 0)
            addView(view)
            layoutNextView(view)
            i++
        } while (i < itemCount)
    }

    private fun performLayout(view: View, viewCenter: Point, halfViewWidth: Int, halfViewHeight: Int) {
        val top = viewCenter.y - halfViewHeight
        val bottom = viewCenter.y + halfViewHeight

        val left = viewCenter.x - halfViewWidth
        val right = viewCenter.x + halfViewWidth

        layoutDecorated(view, left, top, right, bottom)
    }

    private fun layoutNextView(view: View) {
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

    private fun findNextViewCenter(center: Point, halfWidth: Int, halfHeight: Int): Point {
        previousViewCenter = Point(
            paddingLeft + halfWidth,
            center.y + revealPixelHeight
        )
        return previousViewCenter!!
    }
}