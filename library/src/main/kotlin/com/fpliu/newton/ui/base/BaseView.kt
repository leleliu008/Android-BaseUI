package com.fpliu.newton.ui.base

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.fpliu.newton.util.getStatusBarHeight
import com.google.android.material.appbar.AppBarLayout

/**
 * 视图基类
 *
 * @author 792793182@qq.com 2015-09-18.
 */
class BaseView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : CoordinatorLayout(context, attributeSet, defStyleAttr) {

    var appBarLayout: AppBarLayout

    var statusBarPlaceHolder: View

    //标题栏
    var headBarLayout: HeadBarLayout

    //标题栏与Body的分割线
    var separatorView: View

    init {
        id = R.id.base_view

        appBarLayout = AppBarLayout(context)
        if (Build.VERSION.SDK_INT > 20) {
            //对于AppBarLayout来说，使用appBarLayout.elevation是不起作用的
            appBarLayout.targetElevation = 0f
        }
        super.addView(appBarLayout, LayoutParams(MATCH_PARENT, WRAP_CONTENT))

        statusBarPlaceHolder = View(context)
        statusBarPlaceHolder.visibility = View.GONE
        AppBarLayout.LayoutParams(MATCH_PARENT, getStatusBarHeight()).apply {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }.let { appBarLayout.addView(statusBarPlaceHolder, it) }

        headBarLayout = HeadBarLayout(context)
        AppBarLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }.let { appBarLayout.addView(headBarLayout, it) }

        separatorView = View(context)
        AppBarLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }.let { appBarLayout.addView(separatorView, it) }
    }

    override fun addView(view: View, lp: ViewGroup.LayoutParams) {
        val lp2: LayoutParams = if (lp is LayoutParams) lp else LayoutParams(lp.width, lp.height).apply {
            if (lp is MarginLayoutParams) {
                topMargin = lp.topMargin
                leftMargin = lp.leftMargin
                rightMargin = lp.rightMargin
                bottomMargin = lp.bottomMargin

                when (lp) {
                    is FrameLayout.LayoutParams -> gravity = lp.gravity
                    is LinearLayout.LayoutParams -> gravity = lp.gravity
                    is DrawerLayout.LayoutParams -> gravity = lp.gravity
                    is ViewPager.LayoutParams -> gravity = lp.gravity
                }
            }
        }
        super.addView(view, lp2)
    }

    override fun addView(view: View) {
        val lp = view.layoutParams
            ?: LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        addView(view, lp)
    }

    fun addViewMatchParent(view: View) {
        val lp = view.layoutParams
            ?: LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        addView(view, lp)
    }

    fun addView(@LayoutRes layoutId: Int): View {
        val view = inflate(context, layoutId, this)
        removeView(appBarLayout)
        super.addView(appBarLayout, LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        return view
    }

    /**
     * 隐藏掉头部
     */
    fun hideHeadView() {
        headBarLayout.visibility = View.GONE
    }

    /**
     * 显示头部
     */
    fun showHeadView() {
        headBarLayout.visibility = View.VISIBLE
    }

    fun setHeadView(view: View, lp: AppBarLayout.LayoutParams) {
        appBarLayout.removeView(headBarLayout)
        appBarLayout.addView(view, -1, lp)
    }

    fun setHeadView(@LayoutRes layoutId: Int) {
        appBarLayout.removeView(headBarLayout)
        inflate(context, layoutId, appBarLayout)

    }

    fun setHeadBackgroundColor(@ColorInt color: Int) {
        headBarLayout.setBackgroundColor(color)
    }

    fun setHeadBackgroundResource(@DrawableRes resId: Int) {
        headBarLayout.setBackgroundResource(resId)
    }

    fun setHeadBackgroundDrawable(drawable: Drawable) {
        headBarLayout.setBackgroundDrawable(drawable)
    }
}
