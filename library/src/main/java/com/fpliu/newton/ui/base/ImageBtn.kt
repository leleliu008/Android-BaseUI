package com.fpliu.newton.ui.base

import android.content.Context
import android.widget.ImageButton

/**
 * 图片按钮
 *
 * @author 792793182@qq.com 2016-05-31.
 */
open class ImageBtn : IHeadViewStrategy<ImageButton> {

    override fun onCreateView(context: Context): ImageButton {
        return ImageButton(context).apply {
            setImageResource(R.drawable.ic_back_white)
            setBackgroundResource(R.drawable.state_list_rectangle_transparent)
            val padding = UIUtil.dip2px(context, 20.0)
            setPadding(padding, 0, padding, 0)
        }
    }
}
