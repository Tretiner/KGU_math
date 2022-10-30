package com.will.kgu_math.fragments.pdf_viewer

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.max
import kotlin.math.min

class ZoomerRecyclerView : RecyclerView {
    private var maxWidth = 0.0f
    private var maxHeight = 0.0f
    private var width = 0f
    private var height = 0f

    private var mActivePointerId = INVALID_POINTER_ID

    private lateinit var mScaleDetector: ScaleGestureDetector
    private var mScaleFactor = 1f
    private var minScale = 1f
    private var maxScale = 1.5f

    private lateinit var gestureDetector: GestureDetector
    private var mLastTouchX = 0f
    private var mLastTouchY = 0f
    private var mPosX = 0f
    private var mPosY = 0f

    constructor(context: Context) : super(context) {
        if (!isInEditMode) {
            mScaleDetector = ScaleGestureDetector(getContext(), ScaleListener())
            gestureDetector = GestureDetector(getContext(), GestureListener())
        }
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode) {
            mScaleDetector = ScaleGestureDetector(getContext(), ScaleListener())
            gestureDetector = GestureDetector(getContext(), GestureListener())
        }
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        if (!isInEditMode) {
            mScaleDetector = ScaleGestureDetector(getContext(), ScaleListener())
            gestureDetector = GestureDetector(getContext(), GestureListener())
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        width = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        height = MeasureSpec.getSize(heightMeasureSpec).toFloat()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (ex: IllegalArgumentException) {
            ex.printStackTrace()
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        super.onTouchEvent(event)

        val action = event.action

        mScaleDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)

        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> { // pressed
                mLastTouchX = event.x
                mLastTouchY = event.y
                mActivePointerId = event.getPointerId(0)
            }

            MotionEvent.ACTION_MOVE -> { // moved
                val pointerIndex = (action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT)
                val x = event.getX(pointerIndex)
                val y = event.getY(pointerIndex)
                val dx = x - mLastTouchX
                val dy = y - mLastTouchY
                mPosX += dx
                mPosY += dy

                if (mPosX > 0.0f) mPosX = 0.0f else if (mPosX < maxWidth) mPosX = maxWidth
                if (mPosY > 0.0f) mPosY = 0.0f else if (mPosY < maxHeight) mPosY = maxHeight
                mLastTouchX = x
                mLastTouchY = y
                invalidate()
            }
            MotionEvent.ACTION_UP -> { // released
                mActivePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER_ID
            }
            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex = action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = event.getPointerId(pointerIndex)
                if (pointerId == mActivePointerId) {
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    mLastTouchX = event.getX(newPointerIndex)
                    mLastTouchY = event.getY(newPointerIndex)
                    mActivePointerId = event.getPointerId(newPointerIndex)
                }
            }
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(mPosX, mPosY)
        canvas.scale(mScaleFactor, mScaleFactor)
        canvas.restore()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()
        if (mScaleFactor == 1.0f) {
            mPosX = 0.0f
            mPosY = 0.0f
        }
        canvas.translate(mPosX, mPosY)
        canvas.scale(mScaleFactor, mScaleFactor)
        super.dispatchDraw(canvas)
        canvas.restore()
        invalidate()
    }

    private inner class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            mScaleFactor *= detector.scaleFactor
            mScaleFactor = max(1.0f, min(mScaleFactor, 3.0f))
            maxWidth = width - width * mScaleFactor
            maxHeight = height - height * mScaleFactor
            invalidate()
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            mScaleFactor = if (mScaleFactor == maxScale) minScale else maxScale
            invalidate()
            return false
        }
    }

    companion object {
        private const val INVALID_POINTER_ID = -1
    }
}