package com.fpliu.newton.ui.base

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.BounceInterpolator
import android.widget.TextView
import androidx.annotation.IntRange
import androidx.annotation.StringRes
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicReference

/**
 * 顶部类toast弹出层
 */
class ToastLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : TextView(context, attributeSet, defStyleAttr) {

    private enum class State {
        IDLE,          //空闲状态
        ANIM_TO_SHOW,  //正在进入动画
        SHOWING,       //正在展示中
        SHOWING_EX,    //正在展示中，永远不会停，除非用户主动dismiss他
        ANIM_TO_DISMISS//正在退出动画
    }

    private val currentState = AtomicReference(State.IDLE)

    private val interpolator by lazy { BounceInterpolator() }

    private val queue: Queue<Item> = ArrayDeque<Item>()

    private var disposable: Disposable? = null

    private var animator: Animator? = null

    private var translation: Float = 0f

    private var currentShowingItem = AtomicReference<Item>()

    init {
        setBackgroundColor(BaseUIConfig.toastLayoutBgColor)
        setTextColor(Color.WHITE)
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        gravity = Gravity.CENTER
        viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                val topMargin = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin?.toFloat()
                    ?: 0f
                translation = measuredHeight.toFloat() + topMargin
                translationY = -translation
            }
        })
    }

    fun show(@StringRes textId: Int, @IntRange(from = 0) remainTime: Long = 3) {
        show(context.resources.getString(textId), remainTime)
    }

    fun show(text: String, @IntRange(from = 0) remainTime: Long = 3) {
        val item = Item(text, remainTime)
        if (currentState.compareAndSet(State.IDLE, State.ANIM_TO_SHOW)) {
            startShow(item)
        } else {
            //与当前正在展示的一样，就忽略掉
            if (item != currentShowingItem.get()) {
                if (currentState.compareAndSet(State.SHOWING_EX, State.SHOWING)) {
                    showing(item)
                } else {
                    queue.add(item)
                }
            }
        }
    }

    private fun startShow(item: Item) {
        currentState.set(State.ANIM_TO_SHOW)
        currentShowingItem.set(item)
        text = item.text
        animator = ObjectAnimator.ofFloat(this, "translationY", translationY, 0f).apply {
            duration = 500
            interpolator = this@ToastLayout.interpolator
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    showing(item)
                }
            })
            start()
        }
    }

    private fun showing(item: Item) {
        currentState.set(State.SHOWING)
        currentShowingItem.set(item)

        text = item.text

        //显示一段时间之后查看要不要关闭
        val remainTime = item.remainTime
        disposable = Observable
            .timer(if (remainTime == 0L) 3 else remainTime, TimeUnit.SECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                disposable?.run {
                    if (!isDisposed) {
                        dispose()
                    }
                }
                val newItem = queue.poll()
                if (newItem == null) { //队列为空了
                    if (remainTime == 0L) {
                        currentState.set(State.SHOWING_EX)
                    } else {
                        dismiss()
                    }
                } else { //队列中有数据，直接展示
                    text = newItem.text
                    showing(newItem)
                }
            }
    }

    fun dismiss() {
        if (currentState.compareAndSet(State.SHOWING, State.ANIM_TO_DISMISS)
            || currentState.compareAndSet(State.SHOWING_EX, State.ANIM_TO_DISMISS)) {
            animator = ObjectAnimator.ofFloat(this, "translationY", 0f, -translation).apply {
                duration = 1000
                interpolator = this@ToastLayout.interpolator
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        val newItem = queue.poll()
                        if (newItem == null) { //队列为空了
                            currentState.set(State.IDLE)
                            currentShowingItem.set(null)
                        } else { //队列中有数据，重新开始动画
                            startShow(newItem)
                        }
                    }
                })
                start()
            }
        }
    }

    private data class Item(val text: String, val remainTime: Long)
}