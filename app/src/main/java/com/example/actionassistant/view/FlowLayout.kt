package com.example.actionassistant.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.children
import com.example.actionassistant.R

class FlowLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context,attributeSet,defStyle) {

    private val TAG = javaClass.simpleName
    private var mLeft = 0
    private var mTop = 0
    private var lastViewHeight = 0 // 上一个view的高度
    private var mPadding = 0f
    private var mSpace = 0f

    init {
        context.obtainStyledAttributes(attributeSet,R.styleable.FlowLayout).apply {
            mPadding = getDimension(R.styleable.FlowLayout_flow_padding,0f)
            mSpace = getDimension(R.styleable.FlowLayout_flow_space,0f)
            recycle()
            mLeft = mPadding.toInt()
            mTop = mPadding.toInt()
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        children.forEach {
            val w = it.measuredWidth
            val h = it.measuredHeight
            if(mLeft + w + mSpace> width) {
                mLeft = mPadding.toInt()
                mTop += lastViewHeight + mSpace.toInt()
            }
            it.layout(mLeft,mTop,mLeft+w,mTop+h)
            lastViewHeight = h
            mLeft+=w+mSpace.toInt()
        }
    }
}