package eu.kanade.tachiyomi.ui.reader.viewer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import eu.kanade.tachiyomi.util.system.getResourceColor
import kotlin.math.min
import org.nekomanga.R

/** A custom progress bar that always rotates while being determinate. */
class ReaderProgressBar
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    View(context, attrs, defStyleAttr) {

    /**
     * The current sweep angle. It always starts at 10% because otherwise the bar and the rotation
     * wouldn't be visible.
     */
    private var sweepAngle = 10f

    /** Whether the parent views are also visible. */
    private var aggregatedIsVisible = false

    /** The paint to use to draw the progress bar. */
    private var paint = setPaint()

    // Removed setForegroundTintList override - no animation needed

    private fun setPaint(): Paint {
        return Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color =
                foregroundTintList?.defaultColor ?: context.getResourceColor(R.attr.colorSecondary)
            isAntiAlias = true
            strokeCap = Paint.Cap.ROUND
            style = Paint.Style.STROKE
        }
    }

    /**
     * The rectangle of the canvas where the progress bar should be drawn. This is calculated on
     * layout.
     */
    private val ovalRect = RectF()

    /**
     * Called when the view is layout. The position and thickness of the progress bar is calculated.
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        val diameter = min(width, height)
        val thickness = diameter / 10f
        val pad = thickness / 2f
        ovalRect.set(pad, pad, diameter - pad, diameter - pad)

        paint.strokeWidth = thickness
    }

    /**
     * Called when the view is being drawn. An arc is drawn with the calculated rectangle. The
     * animation will take care of rotation.
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawArc(ovalRect, -90f, sweepAngle, false, paint)
    }

    /** Calculates the sweep angle to use from the progress. */
    private fun calcSweepAngleFromProgress(progress: Int): Float {
        return 360f / 100 * progress
    }

    /** Hides this progress bar with an optional fade out if [animate] is true. */
    fun hide(animate: Boolean = false) {
        if (visibility == GONE) return

        if (!animate) {
            visibility = GONE
        } else {
            animate()
                .alpha(0f)
                .setDuration(1000)
                .withEndAction {
                    visibility = GONE
                    alpha = 1f
                }
                .start()
        }
    }

    /** Completes this progress bar and fades out the view. */
    fun completeAndFadeOut() {
        setRealProgress(100)
        hide(true)
    }

    /**
     * Set progress of the circular progress bar ensuring a min max range in order to notice the
     * rotation animation.
     */
    fun setProgress(progress: Int) {
        // Scale progress in [10, 95] range
        val scaledProgress = 85 * progress / 100 + 10
        setRealProgress(scaledProgress)
    }

    /**
     * Sets the real progress of the circular progress bar. Note that if this progres is 0 or 100,
     * the rotation animation won't be noticed by the user because nothing changes in the canvas.
     */
    private fun setRealProgress(progress: Int) {
        sweepAngle = calcSweepAngleFromProgress(progress)
        invalidate()
    }
}
