package com.fpliu.newton.ui.base

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.widget.Button
import android.widget.RelativeLayout

/**
 * 文字按钮
 *
 * @author 792793182@qq.com 2016-05-31.
 */
open class TextBtn : IHeadViewStrategy<Button> {

    override fun onCreateView(context: Context): Button {
        return Button(context).apply {
            text = "提交"
            setBackgroundColor(Color.TRANSPARENT)
            setTextColor(UIUtil.newColorStateList(Color.GREEN, Color.GREEN, Color.GREEN, Color.GRAY))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            layoutParams = RelativeLayout.LayoutParams(UIUtil.dip2px(context, 60.0), RelativeLayout.LayoutParams.WRAP_CONTENT)
        }
    }
}
