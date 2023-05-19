package com.will.kgu_math.fragments.pdf_viewer

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

import androidx.recyclerview.widget.RecyclerView
import java.lang.Exception
import java.lang.Float.max
import java.lang.Float.min

class ZoomerRecyclerView : RecyclerView {

    private var mScaleGestureDetector: ScaleGestureDetector

    private var mScaleFactor: Float = 1f

    private var mMaxWidth: Float = 0f

    private var mLastTouchX: Float = 0f

    private var mTouchX: Float = 0f

    private var mWidth: Float = 0f

    private var mIsZoomEnabled: Boolean = true

    private var mMaxZoom: Float = 3f

    private var mMinZoom: Float = 1f

    private var mQuality: Int = 0

    private var mPosition = -1

    init {
        mScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    fun setMaxZoom(maxZoom: Float) {
        mMaxZoom = maxZoom
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        mWidth = MeasureSpec.getSize(widthMeasureSpec).toFloat()
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        try {
            return super.onInterceptTouchEvent(ev)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

        return false
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        super.onTouchEvent(ev)
        performClick()
        val action = ev.action
        mScaleGestureDetector.onTouchEvent(ev)
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                val x = ev.x
                mLastTouchX = x
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val x = ev.getX(pointerIndex)
                val dx = x - mLastTouchX

                mTouchX += dx

                if (mTouchX > 0f)
                    mTouchX = 0f
                else if (mTouchX < mMaxWidth)
                    mTouchX = mMaxWidth

                mLastTouchX = x
                invalidate()
            }

            MotionEvent.ACTION_POINTER_UP -> {
                val pointerIndex =
                    action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val newPointerIndex = if (pointerIndex == 0) 1 else 0
                mLastTouchX = ev.getX(newPointerIndex)
            }
        }

        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(mTouchX, 0f)
        canvas.scale(mScaleFactor, mScaleFactor)
        canvas.restore()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()

        if (mScaleFactor == mMinZoom) {
            mTouchX = 0f
        }

        canvas.translate(mTouchX, 0f)
        canvas.scale(mScaleFactor, mScaleFactor)

        super.dispatchDraw(canvas)

        canvas.restore()
        invalidate()
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            if (mIsZoomEnabled) {
                mScaleFactor *= detector.scaleFactor
                mScaleFactor = max(mMinZoom, min(mScaleFactor, mMaxZoom))
                mMaxWidth = mWidth - mWidth * mScaleFactor
                invalidate()
            }
            return true
        }
    }
}