package com.fpliu.newton.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.support.annotation.IntRange
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * 顶部类toast弹出层
 */
class ToastLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attributeSet, defStyleAttr) {

    private val interpolator by lazy { BounceInterpolator() }

    private val queue: Queue<Item> by lazy { ArrayDeque<Item>() }

    private var animator: Animator? = null

    init {
        setBackgroundColor(Color.parseColor("#2ed5f9"))
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        gravity = Gravity.CENTER
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                translationY = -measuredHeight.toFloat()
            }
        })
    }

    fun show(@StringRes textId: Int, @IntRange(from = 0) remainTime: Long = 0) {
        show(context.resources.getString(textId), remainTime)
    }

    fun show(text: String, @IntRange(from = 0) remainTime: Long = 0) {
        if (queue.isEmpty() && animator == null) {
            setText(text)
            animator = ObjectAnimator.ofFloat(this, "translationY", translationY, 0f).apply {
                duration = 500
                interpolator = this@ToastLayout.interpolator
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        if (remainTime == 0L) {
                            val item = queue.poll()
                            if (item != null) {
                                setText(item.text)
                            }
                        } else {
                            Observable
                                .timer(remainTime, TimeUnit.SECONDS)
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe { dismiss() }
                        }
                    }
                })
                start()
            }
        } else {
            queue.add(Item(text, remainTime))
        }
    }

    fun dismiss() {
        animator = ObjectAnimator.ofFloat(this, "translationY", 0f, -measuredHeight.toFloat()).apply {
            duration = 1000
            interpolator = this@ToastLayout.interpolator
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    animator = null
                }
            })
            start()
        }
    }

    private class Item(val text: String, val remainTime: Long)
}