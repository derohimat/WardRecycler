package com.wardabbass.redit.ui.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class NonSwipeCrossFadeViewPager @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : androidx.viewpager.widget.ViewPager(context, attrs) {
    var swipeEnabled = false
    var enableTouch = true


    init {
        setPageTransformer(false) { view, position ->
            if (position <= -1.0F || position >= 1.0F) {        // [-Infinity,-1) OR (1,+Infinity]
                view.alpha = 0.0F
                view.visibility = View.GONE
            } else if (position == 0.0F) {     // [0]
                view.alpha = 1.0F
                view.visibility = View.VISIBLE
            } else {

                // Position is between [-1,1]
                view.alpha = 1.0F - Math.abs(position)
                //  view.setTranslationX(-position * (view.getWidth() / 2));
                view.visibility = View.VISIBLE
            }

        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (swipeEnabled) {
                return super.onInterceptTouchEvent(ev)
            }
        } catch (e: Exception) {

        }

        return false
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        try {
            if (!enableTouch) {
                return true
            }
            if (swipeEnabled) {
                return super.onTouchEvent(ev)
            }
        } catch (e: Exception) {

        }

        return false
    }
}
