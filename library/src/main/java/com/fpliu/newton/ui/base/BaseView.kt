package com.fpliu.newton.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.LayoutRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.fpliu.newton.log.Logger
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

    companion object {
        private val TAG = BaseView::class.java.simpleName
    }

    var appBarLayout: AppBarLayout

    var statusBarPlaceHolder: View

    //标题栏
    var headBarLayout: HeadBarLayout

    //标题栏与Body的分割线
    var separatorView: View

    private var networkChangeListener: NetworkChangeListener? = null

    private var receiver: BroadcastReceiver? = object : BroadcastReceiver() {

        override fun onReceive(context: Context, intent: Intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION == intent.action) {
                networkChangeListener?.onNetworkChange(UIUtil.isNetworkAvailable(context))
            }
        }
    }

    init {
        id = R.id.base_view
        setBackgroundColor(BaseUIConfig.bgColor)

        appBarLayout = AppBarLayout(context)
        if (Build.VERSION.SDK_INT > 20) {
            //对于AppBarLayout来说，使用appBarLayout.elevation是不起作用的
            appBarLayout.targetElevation = BaseUIConfig.appBarLayoutElevation
        }
        super.addView(appBarLayout, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        statusBarPlaceHolder = View(context)
        statusBarPlaceHolder.visibility = View.GONE
        AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, UIUtil.getStatusBarHeight(context)).apply {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }.let { appBarLayout.addView(statusBarPlaceHolder, it) }

        headBarLayout = HeadBarLayout(context)
        AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.headHeight).apply {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }.let { appBarLayout.addView(headBarLayout, it) }

        separatorView = View(context)
        if (BaseUIConfig.separatorBg != null) {
            separatorView.background = BaseUIConfig.separatorBg
        }
        AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.separatorHeight).apply {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }.let { appBarLayout.addView(separatorView, it) }
    }

    override fun addView(view: View, lp: ViewGroup.LayoutParams) {
        val lp2: CoordinatorLayout.LayoutParams =
            if (lp is CoordinatorLayout.LayoutParams) lp
            else {
                CoordinatorLayout.LayoutParams(lp.width, lp.height).apply {
                    if (lp is ViewGroup.MarginLayoutParams) {
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
            }
        super.addView(view, lp2)
    }

    override fun addView(view: View) {
        val lp = view.layoutParams
            ?: CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT).apply {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
        addView(view, lp)
    }

    fun addView(@LayoutRes layoutId: Int): View {
        return View.inflate(context, layoutId, null).apply {
            val lp = CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT).apply {
                behavior = AppBarLayout.ScrollingViewBehavior()
            }
            addView(this, lp)
        }
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

    fun setHeadView(view: View) {
        appBarLayout.removeView(headBarLayout)
        AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.headHeight).apply {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }.let { appBarLayout.addView(view, it) }
    }

    fun setHeadView(@LayoutRes layoutId: Int) {
        return setHeadView(LayoutInflater.from(context).inflate(layoutId, appBarLayout, false))
    }

    fun setHeadBackgroundColor(@ColorInt color: Int) {
        headBarLayout.setBackgroundColor(color)
    }

    fun setHeadBackgroundResource(resId: Int) {
        headBarLayout.setBackgroundResource(resId)
    }

    fun setHeadBackgroundDrawable(drawable: Drawable) {
        headBarLayout.setBackgroundDrawable(drawable)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        //注册网络变化的监听器
        IntentFilter().apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        }.let {
            context.registerReceiver(receiver, it)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        try {
            context.unregisterReceiver(receiver)
        } catch (e: Exception) {
            Logger.e(TAG, "onDetachedFromWindow()", e)
        }

        receiver = null
    }

    interface NetworkChangeListener {
        /**
         * 网络变化的回调
         *
         * @param isNetworkAvailable 网络是否可用
         */
        fun onNetworkChange(isNetworkAvailable: Boolean)
    }

    fun setNetworkChangeListener(networkChangeListener: NetworkChangeListener) {
        this.networkChangeListener = networkChangeListener
    }
}
