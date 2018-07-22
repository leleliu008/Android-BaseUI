package com.fpliu.newton.ui.base

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.annotation.FloatRange
import android.view.Gravity
import android.widget.Button
import android.widget.RelativeLayout

/**
 * 全局配置
 *
 * @author 792793182@qq.com 2017-06-11.
 */
object BaseUIConfig {

    /**
     * 设置背景颜色
     *
     * @param bgColor 背景颜色
     */
    @ColorInt
    var bgColor: Int = Color.parseColor("#F2F2F2")

    /**
     * 设置标题栏的背景
     *
     * @param headBg 标题栏的背景
     */
    var headBg: Drawable = ColorDrawable(Color.parseColor("#00aff0"))

    /**
     * 设置标题栏的高度）
     *
     * @param headHeight 标题栏的高度，单位：px
     */
    var headHeight = 60

    /**
     * 设置标题栏两边的边距
     *
     * @param titlePaddingLeftRight 标题栏两边的边距，单位：px
     */
    var titlePaddingLeftRight = 60

    @ColorInt
    var titleColor = Color.WHITE

    var titleSizeSp = 20

    //appBarLayout的阴影
    @FloatRange(from = 0.0)
    var appBarLayoutElevation: Float = 0f

    /**
     * 设置字体
     *
     * @param fontFileName 字体文件存放在assets目录中
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
