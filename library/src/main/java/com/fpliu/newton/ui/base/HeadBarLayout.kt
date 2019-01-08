package com.fpliu.newton.ui.base

import android.content.ContentValues.TAG
import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.fpliu.newton.log.Logger
import io.reactivex.Observable

class HeadBarLayout @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attributeSet, defStyleAttr) {

    /**
     * 标题栏文本
     */
    private var titleTv: TextView

    /**
     * 标题栏左边的按钮
     */
    private var leftView: View? = null

    /**
     * 标题栏右边的按钮
     */
    private var rightView: View? = null

    init {
        id = R.id.base_view_head
        visibility = View.GONE
        setBackgroundDrawable(BaseUIConfig.headBg)

        titleTv = TextView(context).apply {
            setTextColor(BaseUIConfig.titleColor)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, BaseUIConfig.titleSizeSp.toFloat())
            gravity = Gravity.CENTER
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }

        val lp = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT).apply {
            leftMargin = UIUtil.dip2px(context, BaseUIConfig.titlePaddingLeftRight.toDouble())
            rightMargin = leftMargin
            addRule(RelativeLayout.CENTER_IN_PARENT)
        }

        addView(titleTv, lp)
    }

    fun setTitle(title: CharSequence) {
        titleTv.text = title
        visibility = View.VISIBLE
    }

    fun setTitle(@StringRes titleStringId: Int) {
        try {
            setTitle(context.getString(titleStringId))
        } catch (e: Exception) {
            Logger.e(TAG, "setTitle()", e)
        }
    }

    fun setTitleTextSize(@FloatRange(from = 0.0) size: Float) {
        titleTv.textSize = size
    }

    fun setTitleTextColor(@ColorInt color: Int) {
        titleTv.setTextColor(color)
    }

    fun setTitleTextColorRes(@ColorRes colorRes: Int) {
        titleTv.setTextColor(ContextCompat.getColor(context, colorRes))
    }

    fun getLeftBtnClickObservable(): Observable<View> {
        return if (leftView == null) Observable.empty() else ViewClickObservable(leftView!!)
    }

    fun getRightBtnClickObservable(): Observable<View> {
        return if (rightView == null) Observable.empty() else ViewClickObservable(rightView!!)
    }

    fun setLeftViewStrategy(headViewStrategy: IHeadViewStrategy<*>?) {
        if (headViewStrategy == null) {
            if (leftView?.parent != null) {
                removeView(leftView)
                leftView = null
            }
        } else {
            val view = headViewStrategy.onCreateView(context)
            if (view.parent == null) {
                if (leftView?.parent != null) {
                    removeView(leftView)
                }

                val lp = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                addView(view, lp)
                leftView = view
            }
        }
    }

    fun setRightViewStrategy(headViewStrategy: IHeadViewStrategy<*>?) {
        if (headViewStrategy == null) {
            if (rightView?.parent != null) {
                removeView(rightView)
                rightView = null
            }
        } else {
            val view = headViewStrategy.onCreateView(context)
            if (view.parent == null) {
                if (rightView?.parent != null) {
                    removeView(rightView)
                }
                val lp = RelativeLayout.LayoutParams(CoordinatorLayout.LayoutParams.WRAP_CONTENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                addView(view, lp)
                rightView = view
            }
        }
    }

    fun addHeadViewStrategy(headViewStrategy: IHeadViewStrategy<*>) {
        headViewStrategy.onCreateView(context)
    }
}