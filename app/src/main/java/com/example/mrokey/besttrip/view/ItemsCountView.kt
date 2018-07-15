package com.example.mrokey.besttrip.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.animation.*
import android.widget.LinearLayout
import android.widget.TextSwitcher
import android.widget.TextView
import com.example.mrokey.besttrip.R

class ItemsCountView : LinearLayout {

    private var textSwitcher: TextSwitcher? = null
    private var textView: TextView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    private fun init(context: Context) {
        textSwitcher = TextSwitcher(context)
        textSwitcher!!.addView(createViewForTextSwitcher(context))
        textSwitcher!!.addView(createViewForTextSwitcher(context))

        addView(textSwitcher, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))

        textView = TextView(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView!!.setTextAppearance(R.style.positionIndicator)
        } else {
            textView!!.setTextAppearance(context, R.style.positionIndicator)
        }

        addView(textView, LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    private fun createViewForTextSwitcher(context: Context): TextView {
        val textView = TextView(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextAppearance(R.style.positionIndicatorCurrent)
        } else {
            textView.setTextAppearance(context, R.style.positionIndicatorCurrent)
        }
        return textView
    }

    @SuppressLint("SetTextI18n")
    fun update(newPosition: Int, oldPosition: Int, totalElements: Int) {
        textView!!.text = " / $totalElements"
        val offset = (textSwitcher!!.height * 0.75).toInt()
        val duration = 250
        if (newPosition > oldPosition) {
            textSwitcher!!.inAnimation = createPositionAnimation(-offset, 0, 0f, 1f, duration)
            textSwitcher!!.outAnimation = createPositionAnimation(0, offset, 1f, 0f, duration)
        } else if (oldPosition > newPosition) {
            textSwitcher!!.inAnimation = createPositionAnimation(offset, 0, 0f, 1f, duration)
            textSwitcher!!.outAnimation = createPositionAnimation(0, -offset, 1f, 0f, duration)
        }
        textSwitcher!!.setText((newPosition + 1).toString())
    }

    private fun createPositionAnimation(fromY: Int, toY: Int, fromAlpha: Float, toAlpha: Float, duration: Int): Animation {
        val translate = TranslateAnimation(0f, 0f, fromY.toFloat(), toY.toFloat())
        translate.duration = duration.toLong()

        val alpha = AlphaAnimation(fromAlpha, toAlpha)
        alpha.duration = duration.toLong()

        val set = AnimationSet(true)
        set.interpolator = DecelerateInterpolator()
        set.addAnimation(translate)
        set.addAnimation(alpha)
        return set
    }


}