package app.demoktx.carousel

import android.content.Context
import android.os.Handler
import android.util.AttributeSet
import android.view.View

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

import app.demoktx.R


/**
 * A ViewPager that auto-scrolls, and supports infinite scroll.
 * For infinite Scroll, you may use LoopingPagerAdapter.
 */

class LoopingViewPager : ViewPager {

    protected var isInfinite = true
    protected var isAutoScroll = false
    protected var wrapContent = true
    protected var aspectRatio: Float = 0.toFloat()

    //AutoScroll
    private var interval = 5000
    private var previousPosition = 0
    private var currentPagePosition = 0
    private val autoScrollHandler = Handler()
    private val autoScrollRunnable = Runnable {
        if (adapter == null || !isAutoScroll || adapter!!.count < 2) return@Runnable
        if (!isInfinite && adapter!!.count - 1 == currentPagePosition) {
            currentPagePosition = 0
        } else {
            currentPagePosition++
        }
        setCurrentItem(currentPagePosition, true)
    }

    //For Indicator
    private var indicatorPageChangeListener: IndicatorPageChangeListener? = null
    private var previousScrollState = ViewPager.SCROLL_STATE_IDLE
    private var scrollState = ViewPager.SCROLL_STATE_IDLE
    private var isToTheRight = true
    /**
     * This boolean indicates whether LoopingViewPager needs to continuously tell the indicator about
     * the progress of the scroll, even after onIndicatorPageChange().
     * If indicator is smart, it should be able to finish the animation by itself after we told it that a position has been selected.
     * If indicator is not smart, then LoopingViewPager will continue to fire onIndicatorProgress() to update the indicator
     * transition position.
     */
    private var isIndicatorSmart = false


    /**
     * A method that helps you integrate a ViewPager Indicator.
     * This method returns the expected position (Starting from 0) of indicators.
     * This method should be used after currentPagePosition is updated.
     */
    //Dummy last item is selected. Indicator should be at the last one
    //Dummy first item is selected. Indicator should be at the first one
    val indicatorPosition: Int
        get() {
            if (!isInfinite) {
                return currentPagePosition
            } else {
                if (adapter !is LoopingPagerAdapter<*>) return currentPagePosition
                return if (currentPagePosition == 0) {
                    (adapter as LoopingPagerAdapter<*>).count - 1
                } else if (currentPagePosition == (adapter as LoopingPagerAdapter<*>).lastItemPosition + 1) {
                    0
                } else {
                    currentPagePosition - 1
                }
            }
        }

    /**
     * A method that helps you integrate a ViewPager Indicator.
     * This method returns the expected count of indicators.
     */
    val indicatorCount: Int
        get() = if (adapter is LoopingPagerAdapter<*>) {
            (adapter as LoopingPagerAdapter<*>).count
        } else {
            adapter!!.count
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(attrs, R.styleable.LoopingViewPager, 0, 0)
        try {
            isInfinite = a.getBoolean(R.styleable.LoopingViewPager_isInfinite, false)
            isAutoScroll = a.getBoolean(R.styleable.LoopingViewPager_autoScroll, false)
            wrapContent = a.getBoolean(R.styleable.LoopingViewPager_wrap_content, true)
            interval = a.getInt(R.styleable.LoopingViewPager_scrollInterval, 5000)
            aspectRatio = a.getFloat(R.styleable.LoopingViewPager_viewpagerAspectRatio, 0f)
        } finally {
            a.recycle()
        }
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightMeasureSpec = heightMeasureSpec
        if (aspectRatio > 0) {
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            val height = Math.round(View.MeasureSpec.getSize(widthMeasureSpec).toFloat() / aspectRatio)
            val finalWidthMeasureSpec = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
            val finalHeightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
            super.onMeasure(finalWidthMeasureSpec, finalHeightMeasureSpec)
        } else {
            //https://stackoverflow.com/a/24666987/7870874
            if (wrapContent) {
                val mode = View.MeasureSpec.getMode(heightMeasureSpec)
                // Unspecified means that the ViewPager is in a ScrollView WRAP_CONTENT.
                // At Most means that the ViewPager is not in a ScrollView WRAP_CONTENT.
                if (mode == View.MeasureSpec.UNSPECIFIED || mode == View.MeasureSpec.AT_MOST) {
                    // super has to be called in the beginning so the child views can be initialized.
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                    var height = 0
                    for (i in 0 until childCount) {
                        val child = getChildAt(i)
                        child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED))
                        val h = child.measuredHeight
                        if (h > height) height = h
                    }
                    heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
                }
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    protected fun init() {
        addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            internal var currentPosition: Float = 0.toFloat()

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (indicatorPageChangeListener == null) return

                if (position + positionOffset >= currentPosition) {
                    isToTheRight = true
                } else {
                    isToTheRight = false
                }
                if (positionOffset == 0f) currentPosition = position.toFloat()

                val realPosition = getSelectingIndicatorPosition(isToTheRight)

                val progress: Float
                if (scrollState == ViewPager.SCROLL_STATE_SETTLING && Math.abs(currentPagePosition - previousPosition) > 1) {
                    val pageDiff = Math.abs(currentPagePosition - previousPosition)
                    if (isToTheRight) {
                        progress = (position - previousPosition).toFloat() / pageDiff + positionOffset / pageDiff
                    } else {
                        progress = (previousPosition - (position + 1)).toFloat() / pageDiff + (1 - positionOffset) / pageDiff
                    }
                } else {
                    progress = if (isToTheRight) positionOffset else 1 - positionOffset
                }

                if (progress == 0f || progress > 1) return

                if (isIndicatorSmart) {
                    if (scrollState != ViewPager.SCROLL_STATE_DRAGGING) return
                    indicatorPageChangeListener!!.onIndicatorProgress(realPosition, progress)
                } else {
                    if (scrollState == ViewPager.SCROLL_STATE_DRAGGING) {
                        if (isToTheRight && Math.abs(realPosition - currentPagePosition) == 2 || !isToTheRight && realPosition == currentPagePosition) {
                            //If this happens, it means user is fast scrolling where onPageSelected() is not fast enough
                            //to catch up with the scroll, thus produce wrong position value.
                            return
                        }
                    }
                    indicatorPageChangeListener!!.onIndicatorProgress(realPosition, progress)
                }
            }

            override fun onPageSelected(position: Int) {
                previousPosition = currentPagePosition
                currentPagePosition = position
                if (indicatorPageChangeListener != null) {
                    indicatorPageChangeListener!!.onIndicatorPageChange(indicatorPosition)
                }
                autoScrollHandler.removeCallbacks(autoScrollRunnable)
                autoScrollHandler.postDelayed(autoScrollRunnable, interval.toLong())
            }

            override fun onPageScrollStateChanged(state: Int) {
                if (!isIndicatorSmart) {
                    if (scrollState == ViewPager.SCROLL_STATE_SETTLING && state == ViewPager.SCROLL_STATE_DRAGGING) {
                        if (indicatorPageChangeListener != null) {
                            indicatorPageChangeListener!!.onIndicatorProgress(
                                    getSelectingIndicatorPosition(isToTheRight), 1f)
                        }
                    }
                }
                previousScrollState = scrollState
                scrollState = state

                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    //Below are code to achieve infinite scroll.
                    //We silently and immediately flip the item to the first / last.
                    if (isInfinite) {
                        if (adapter == null) return
                        val itemCount = adapter!!.count
                        if (itemCount < 2) {
                            return
                        }
                        val index = currentItem
                        if (index == 0) {
                            setCurrentItem(itemCount - 2, false) //Real last item
                        } else if (index == itemCount - 1) {
                            setCurrentItem(1, false) //Real first item
                        }
                    }

                    if (indicatorPageChangeListener != null) {
                        indicatorPageChangeListener!!.onIndicatorProgress(indicatorPosition, 1f)
                    }
                }
            }
        })
        if (isInfinite) setCurrentItem(1, false)
    }

    override fun setAdapter(adapter: PagerAdapter?) {
        super.setAdapter(adapter)
        if (isInfinite) setCurrentItem(1, false)
    }

    fun resumeAutoScroll() {
        autoScrollHandler.postDelayed(autoScrollRunnable, interval.toLong())
    }

    fun pauseAutoScroll() {
        autoScrollHandler.removeCallbacks(autoScrollRunnable)
    }

    /**
     * A method that helps you integrate a ViewPager Indicator.
     * This method returns the expected position (Starting from 0) of indicators.
     * This method should be used before currentPagePosition is updated, when user is trying to
     * select a different page, i.e. onPageScrolled() is triggered.
     */
    fun getSelectingIndicatorPosition(isToTheRight: Boolean): Int {
        if (scrollState == ViewPager.SCROLL_STATE_SETTLING || scrollState == ViewPager.SCROLL_STATE_IDLE
                || previousScrollState == ViewPager.SCROLL_STATE_SETTLING && scrollState == ViewPager.SCROLL_STATE_DRAGGING) {
            return indicatorPosition
        }
        val delta = if (isToTheRight) 1 else -1
        if (isInfinite) {
            if (adapter !is LoopingPagerAdapter<*>) return currentPagePosition + delta
            return if (currentPagePosition == 1 && !isToTheRight) { //Special case for first page to last page
                (adapter as LoopingPagerAdapter<*>).lastItemPosition - 1
            } else if (currentPagePosition == (adapter as LoopingPagerAdapter<*>).lastItemPosition && isToTheRight) { //Special case for last page to first page
                0
            } else {
                currentPagePosition + delta - 1
            }
        } else {
            return currentPagePosition + delta
        }
    }


    /**
     * This function needs to be called if dataSet has changed,
     * in order to reset current selected item and currentPagePosition and autoPageSelectionLock.
     */
    fun reset() {
        if (isInfinite) {
            setCurrentItem(1, false)
            currentPagePosition = 1
        } else {
            setCurrentItem(0, false)
            currentPagePosition = 0
        }
    }

    fun setIndicatorSmart(isIndicatorSmart: Boolean) {
        this.isIndicatorSmart = isIndicatorSmart
    }

    fun setIndicatorPageChangeListener(callback: IndicatorPageChangeListener) {
        this.indicatorPageChangeListener = callback
    }

    interface IndicatorPageChangeListener {
        fun onIndicatorProgress(selectingPosition: Int, progress: Float)

        fun onIndicatorPageChange(newIndicatorPosition: Int)
    }

}
