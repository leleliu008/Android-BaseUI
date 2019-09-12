package com.fpliu.newton.ui.base

import android.content.Context
import android.graphics.Color
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.FloatRange
import androidx.annotation.StringRes
import com.fpliu.newton.util.dp2px
import com.fpliu.newton.util.setTextColorRes

class HeadBarLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr) {

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

        titleTv = TextView(context).apply {
            id = R.id.base_view_head_title
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 20f)
            gravity = Gravity.CENTER
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
        }

        val lp = LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
            leftMargin = dp2px(60f)
            rightMargin = leftMargin
            addRule(CENTER_IN_PARENT)
        }

        addView(titleTv, lp)
    }

    fun setTitle(title: CharSequence) {
        titleTv.text = title
        visibility = View.VISIBLE
    }

    fun setTitle(@StringRes titleStringId: Int) {
        setTitle(context.getString(titleStringId))
    }

    fun setTitleTextSize(@FloatRange(from = 0.0) size: Float) {
        titleTv.textSize = size
    }

    fun setTitleTextColor(@ColorInt color: Int) {
        titleTv.setTextColor(color)
    }

    fun setTitleTextColorRes(@ColorRes colorRes: Int) {
        titleTv.setTextColorRes(colorRes)
    }

    fun setLeftView(view: View?, lp: LayoutParams) {
        if (view == null) {
            if (leftView?.parent != null) {
                removeView(leftView)
                leftView = null
            }
        } else {
            if (view.parent == null) {
                if (leftView?.parent != null) {
                    removeView(leftView)
                }
                lp.addRule(ALIGN_PARENT_LEFT)
                addView(view, -1, lp)
                leftView = view
            }
        }
    }

    fun setLeftView(view: View?) {
        setLeftView(view, LayoutParams(WRAP_CONTENT, MATCH_PARENT))
    }

    fun setRightView(view: View?, lp: LayoutParams) {
        if (view == null) {
            if (rightView?.parent != null) {
                removeView(rightView)
                rightView = null
            }
        } else {
            if (view.parent == null) {
                if (rightView?.parent != null) {
                    removeView(rightView)
                }
                lp.addRule(ALIGN_PARENT_RIGHT)
                addView(view, -1, lp)
                rightView = view
            }
        }
    }

    fun setRightView(view: View?) {
        setRightView(view, LayoutParams(WRAP_CONTENT, MATCH_PARENT))
    }
}