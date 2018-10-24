package com.fpliu.newton.base.ui.sample

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.fpliu.newton.ui.base.BaseActivity
import com.fpliu.newton.ui.base.BaseUIConfig
import com.fpliu.newton.ui.base.UIUtil
import com.fpliu.newton.ui.recyclerview.adapter.ItemAdapter
import com.fpliu.newton.ui.recyclerview.holder.ItemViewHolder

/**
 * BaseUI使用示例
 * @author 792793182@qq.com 2018-03-28.
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //统一设置标题栏高度
        val headHeight = UIUtil.dp2px(this, 48)
        BaseUIConfig.headHeight = headHeight


        super.onCreate(savedInstanceState)
        title = "BaseUI使用示例"

        val lp = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT).apply {
            behavior = AppBarLayout.ScrollingViewBehavior()
        }
        val nestedScrollView = NestedScrollView(this).apply {
            isFillViewport = true
            addContentView(this, lp)
        }

        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            nestedScrollView.addView(this)
        }

        ImageView(this).run {
            scaleType = ImageView.ScaleType.FIT_XY
            setImageResource(R.drawable.notify_panel_notification_icon_bg)
            container.addView(this, LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 200))
            setOnClickListener {
                dismissToast()
            }
        }

        val items = arrayListOf<String>()
        repeat(100) {
            items.add("$it")
        }

        RecyclerView(this).run {
            setBackgroundColor(Color.CYAN)
            isFocusableInTouchMode = false
            isNestedScrollingEnabled = false
            addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager.VERTICAL))
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = object : ItemAdapter<String>(items) {
                override fun onBindLayout(parent: ViewGroup, viewType: Int): Int {
                    return R.layout.abc_action_bar_title_item
                }

                override fun onBindViewHolder(holder: ItemViewHolder, position: Int, item: String) {
                    holder.id(R.id.action_bar_title).text(item)
                }
            }
            container.addView(this)
        }

//        Observable.timer(5, TimeUnit.SECONDS)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                showToast("网路状况不佳，请切换Wi-Fi")
//            }

//        Observable
//            .intervalRange(0, 100, 5, 6, TimeUnit.SECONDS)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe {
//                showToast("网路状况不佳，请切换Wi-Fi")
//            }
        var i = 0

        (contentView.toastLayout.layoutParams as CoordinatorLayout.LayoutParams).run {
            topMargin = UIUtil.getStatusBarHeight(window)
            contentView.toastLayout.layoutParams = this
        }

        val fab = FloatingActionButton(this)
        fab.setOnClickListener {
            showToast("网路状况不佳，请切换Wi-Fi")
        }
        val lp2 = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT).apply {
            gravity = Gravity.BOTTOM or Gravity.RIGHT
            bottomMargin = 30
            rightMargin = 30
        }
        addContentView(fab, lp2)
    }
}