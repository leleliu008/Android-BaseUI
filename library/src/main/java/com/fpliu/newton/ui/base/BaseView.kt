package com.fpliu.newton.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.os.Build
import android.support.annotation.ColorInt
import android.support.annotation.LayoutRes
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewPager
import android.support.v4.widget.DrawerLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.fpliu.newton.log.Logger

/**
 * 视图基类
 *
 * @author 792793182@qq.com 2015-09-18.
 */
class BaseView @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : CoordinatorLayout(context, attributeSet, defStyleAttr) {

    companion object {
        private val TAG = BaseView::class.java.simpleName
    }

    var appBarLayout: AppBarLayout

    //提示信息弹出层
    var toastLayout: ToastLayout

    //标题栏
    var headBarLayout: HeadBarLayout

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
        super.addView(appBarLayout, CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.WRAP_CONTENT))

        headBarLayout = HeadBarLayout(context)
        AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.headHeight).apply {
            scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP
        }.let { appBarLayout.addView(headBarLayout, it) }

        toastLayout = ToastLayout(context)
        super.addView(toastLayout, CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.headHeight))
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

        //确保toastLayout在最上层
        removeView(toastLayout)
        super.addView(view, lp2)

        val lp3 = toastLayout.layoutParams
            ?: CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.headHeight)
        super.addView(toastLayout, lp3)
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
        appBarLayout.removeAllViews()
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

    fun showToast(text: String, remainTime: Long = 3) {
        toastLayout.show(text, remainTime)
    }

    fun showToast(text: String) {
        toastLayout.show(text)
    }

    fun showToastDontDismiss(text: String) {
        toastLayout.show(text, 0)
    }

    fun dismissToast() {
        toastLayout.dismiss()
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
