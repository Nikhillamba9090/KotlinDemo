package app.demoktx.carousel

import android.view.View


class ZoomPageTransformer : BasePageTransformer {
    private var mMinScale = 0.85f
    private var mMinAlpha = 0.65f
    private var isShowAlpha = true

    constructor() {}

    constructor(isShowAlpha: Boolean) {
        this.isShowAlpha = isShowAlpha
    }

    constructor(minAlpha: Float, minScale: Float) {
        setMinAlpha(minAlpha)
        setMinScale(minScale)
    }

    override
    fun handleInvisiblePage(view: View, position: Float) {
        if (isShowAlpha) view.alpha = 0f
    }

    override
    fun handleLeftPage(view: View, position: Float) {
        handler(view, position)
    }

    override
    fun handleRightPage(view: View, position: Float) {
        handler(view, position)
    }

    private fun handler(view: View, position: Float) {
        val pageWidth = view.width
        val pageHeight = view.height
        // Modify the default slide transition to shrink the page as well
        val scaleFactor = Math.max(mMinScale, 1 - Math.abs(position))
        val vertMargin = pageHeight * (1 - scaleFactor) / 2
        val horzMargin = pageWidth * (1 - scaleFactor) / 2
        if (position < 0) {
            view.translationX = horzMargin - vertMargin / 2
        } else {
            view.translationX = -horzMargin + vertMargin / 2
        }

        // Scale the page down (between MIN_SCALE and 1)
        view.scaleX = scaleFactor
        view.scaleY = scaleFactor

        // Fade the page relative to its size.
        if (isShowAlpha) {
            view.alpha = mMinAlpha + (scaleFactor - mMinScale) / (1 - mMinScale) * (1 - mMinAlpha)
        }
    }

    fun setMinAlpha(minAlpha: Float) {
        mMinAlpha = minAlpha
    }

    fun setMinScale(minScale: Float) {
        mMinScale = minScale
    }
}