package com.fpliu.newton.ui.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.widget.Button
import android.widget.FrameLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

/**
 * 全局配置
 *
 * @author 792793182@qq.com 2017-06-11.
 */
object BaseUIConfig {

    /**
     * 背景颜色
     */
    @ColorInt
    var bgColor: Int = Color.parseColor("#F2F2F2")

    /**
     * 标题栏的背景
     */
    var headBg: Drawable = ColorDrawable(Color.parseColor("#00aff0"))

    /**
     * 标题栏的高度，单位：px
     */
    var headHeight = 60

    /**
     *  标题栏两边的边距，单位：px
     */
    var titlePaddingLeftRight = 60

    @ColorInt
    var titleColor = Color.WHITE

    var titleSizeSp = 20

    //appBarLayout的阴影
    @FloatRange(from = 0.0)
    var appBarLayoutElevation: Float = 0f

    /**
     * toastLayout背景颜色
     */
    @ColorInt
    var toastLayoutBgColor: Int = Color.parseColor("#00ddbb")

    /**
     * toastLayout的布局参数
     */
    var toastLayoutLayoutParams: FrameLayout.LayoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, headHeight)

    /**
     * 字体文件，存放在assets目录中
     */
    var fontFileName: String? = null

    var leftBtn: IHeadViewStrategy<*> = object : TextBtn() {

        override fun onCreateView(context: Context): Button {
            return super.onCreateView(context).apply {
                text = "返回"
                gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
                setTextColor(Color.WHITE)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_back_normal, 0, 0, 0)
                compoundDrawablePadding = 5
                val padding = UIUtil.dip2px(context, 10.0)
                setPadding(padding, 0, padding, 0)
                layoutParams = RelativeLayout.LayoutParams(UIUtil.dip2px(context, 90.0), RelativeLayout.LayoutParams.WRAP_CONTENT)
            }
        }
    }
}
