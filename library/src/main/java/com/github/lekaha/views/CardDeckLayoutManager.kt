package com.github.lekaha.views

import android.os.Bundle
import android.os.Parcelable
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams

class CardDeckLayoutManager : RecyclerView.LayoutManager() {

    companion object {
        const val FIRST_VISIBLE_POSITION = "FIRST_VISIBLE_POSITION"
        const val LAST_VISIBLE_POSITION = "LAST_VISIBLE_POSITION"
    }

    private var firstVisiblePosition = 0
    private var lastVisiblePosition = 0

    /* Used for tracking off-screen change events */
    private var mFirstChangedPosition: Int = 0
    private var mChangedPositionCount: Int = 0

    /* Consistent size applied to all child views */
    private var mDecoratedChildWidth: Int = 0
    private var mDecoratedChildHeight: Int = 0

    override fun canScrollHorizontally() = false

    override fun generateDefaultLayoutParams(): LayoutParams =
        RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.MATCH_PARENT)

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

    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {

        //We have nothing to show for an empty data set but clear any existing views
        if (itemCount == 0) {
//            removeAllViews()
            detachAndScrapAttachedViews(recycler)
            return
        }
        if (childCount == 0 && state.isPreLayout) {
            //Nothing to do during prelayout when empty
            return
        }

        removeAllViews()

        ///Clear change tracking state when a real layout occurs
        if (!state.isPreLayout) {
            mChangedPositionCount = 0
            mFirstChangedPosition = 0
        }

        if (childCount == 0) { //First or empty layout
            //Scrap measure one child
            val scrap = recycler.getViewForPosition(0)
            addView(scrap)
            measureChildWithMargins(scrap, 0, 0)

            /*
             * We make some assumptions in this code based on every child
             * view being the same size (i.e. a uniform grid). This allows
             * us to compute the following values up front because they
             * won't change.
             */
            mDecoratedChildWidth = getDecoratedMeasuredWidth(scrap);
            mDecoratedChildHeight = getDecoratedMeasuredHeight(scrap);

            detachAndScrapView(scrap, recycler)
        }

        //Always update the visible row/column counts
//        updateWindowSizing()

//        val removedCache: SparseIntArray?
//        /*
//         * During pre-layout, we need to take note of any views that are
//         * being removed in order to handle predictive animations
//         */
//        if (state.isPreLayout) {
//            removedCache = SparseIntArray(childCount)
//
//            for (i in 0..childCount) {
//                val view = getChildAt(i)
//                view?.layoutParams?.is
//            }
//            for (var i=0; i < childCount; i++) {
//                final View view = getChildAt(i);
//                LayoutParams lp = (LayoutParams) view.getLayoutParams();
//
//                if (lp.isItemRemoved()) {
//                    //Track these view removals as visible
//                    removedCache.put(lp.getViewLayoutPosition(), REMOVE_VISIBLE);
//                }
//            }
//
//            //Track view removals that happened out of bounds (i.e. off-screen)
//            if (removedCache.size() == 0 && mChangedPositionCount > 0) {
//                for (int i = mFirstChangedPosition; i < (mFirstChangedPosition + mChangedPositionCount); i++) {
//                    removedCache.put(i, REMOVE_INVISIBLE);
//                }
//            }
//        }
//
//
//        int childLeft;
//        int childTop;
//        if (getChildCount() == 0) { //First or empty layout
//            //Reset the visible and scroll positions
//            mFirstVisiblePosition = 0;
//            childLeft = getPaddingLeft();
//            childTop = getPaddingTop();
//        } else if (!state.isPreLayout()
//            && getVisibleChildCount() >= state.getItemCount()) {
//            //Data set is too small to scroll fully, just reset position
//            mFirstVisiblePosition = 0;
//            childLeft = getPaddingLeft();
//            childTop = getPaddingTop();
//        } else { //Adapter data set changes
//            /*
//             * Keep the existing initial position, and save off
//             * the current scrolled offset.
//             */
//            final View topChild = getChildAt(0);
//            childLeft = getDecoratedLeft(topChild);
//            childTop = getDecoratedTop(topChild);
//
//            /*
//             * When data set is too small to scroll vertically, adjust vertical offset
//             * and shift position to the first row, preserving current column
//             */
//            if (!state.isPreLayout() && getVerticalSpace() > (getTotalRowCount() * mDecoratedChildHeight)) {
//                mFirstVisiblePosition = mFirstVisiblePosition % getTotalColumnCount();
//                childTop = getPaddingTop();
//
//                //If the shift overscrolls the column max, back it off
//                if ((mFirstVisiblePosition + mVisibleColumnCount) > state.getItemCount()) {
//                    mFirstVisiblePosition = Math.max(state.getItemCount() - mVisibleColumnCount, 0);
//                    childLeft = getPaddingLeft();
//                }
//            }
//
//            /*
//             * Adjust the visible position if out of bounds in the
//             * new layout. This occurs when the new item count in an adapter
//             * is much smaller than it was before, and you are scrolled to
//             * a location where no items would exist.
//             */
//            int maxFirstRow = getTotalRowCount() - (mVisibleRowCount-1);
//            int maxFirstCol = getTotalColumnCount() - (mVisibleColumnCount-1);
//            boolean isOutOfRowBounds = getFirstVisibleRow() > maxFirstRow;
//            boolean isOutOfColBounds =  getFirstVisibleColumn() > maxFirstCol;
//            if (isOutOfRowBounds || isOutOfColBounds) {
//                int firstRow;
//                if (isOutOfRowBounds) {
//                    firstRow = maxFirstRow;
//                } else {
//                    firstRow = getFirstVisibleRow();
//                }
//                int firstCol;
//                if (isOutOfColBounds) {
//                    firstCol = maxFirstCol;
//                } else {
//                    firstCol = getFirstVisibleColumn();
//                }
//                mFirstVisiblePosition = firstRow * getTotalColumnCount() + firstCol;
//
//                childLeft = getHorizontalSpace() - (mDecoratedChildWidth * mVisibleColumnCount);
//                childTop = getVerticalSpace() - (mDecoratedChildHeight * mVisibleRowCount);
//
//                //Correct cases where shifting to the bottom-right overscrolls the top-left
//                // This happens on data sets too small to scroll in a direction.
//                if (getFirstVisibleRow() == 0) {
//                    childTop = Math.min(childTop, getPaddingTop());
//                }
//                if (getFirstVisibleColumn() == 0) {
//                    childLeft = Math.min(childLeft, getPaddingLeft());
//                }
//            }
//        }
//
//        //Clear all attached views into the recycle bin
//        detachAndScrapAttachedViews(recycler);
//
//        //Fill the grid for the initial layout of views
//        fillGrid(DIRECTION_NONE, childLeft, childTop, recycler, state, removedCache);
//
//        //Evaluate any disappearing views that may exist
//        if (!state.isPreLayout() && !recycler.getScrapList().isEmpty()) {
//            final List<RecyclerView.ViewHolder> scrapList = recycler.getScrapList();
//            final HashSet<View> disappearingViews = new HashSet<View>(scrapList.size());
//
//            for (RecyclerView.ViewHolder holder : scrapList) {
//                final View child = holder.itemView;
//                final LayoutParams lp = (LayoutParams) child.getLayoutParams();
//                if (!lp.isItemRemoved()) {
//                    disappearingViews.add(child);
//                }
//            }
//
//            for (View child : disappearingViews) {
//                layoutDisappearingView(child);
//            }
//        }
    }
}