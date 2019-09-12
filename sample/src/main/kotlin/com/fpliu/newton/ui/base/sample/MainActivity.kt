package com.fpliu.newton.ui.base.sample

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.RelativeLayout
import com.fpliu.newton.ui.base.BaseActivity
import com.fpliu.newton.util.getDimensionPixelSize
import com.jakewharton.rxbinding3.view.clicks
import com.uber.autodispose.autoDisposable

/**
 * BaseUI使用示例
 * @author 792793182@qq.com 2018-03-28.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title = "BaseUI使用示例"

        createRightView()

        setContentView(R.layout.activity_main)
    }

    override fun onRightBtnClick() {
        showToast("敬请期待")
    }

    private fun createRightView() {
        val rightView = ImageView(this).apply {
            scaleType = ImageView.ScaleType.CENTER_INSIDE
            setImageResource(R.drawable.ic_setting)
            clicks().autoDisposable(disposeOnDestroy()).subscribe { onRightBtnClick() }
        }

        val lp = RelativeLayout.LayoutParams(WRAP_CONTENT, MATCH_PARENT).apply {
            rightMargin = getDimensionPixelSize(R.dimen.dp750_30)
        }

        contentView.headBarLayout.setRightView(rightView, lp)
    }
}
