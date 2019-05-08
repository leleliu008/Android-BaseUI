package com.fpliu.newton.ui.base

import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable

/**
 * Activity界面基类
 *
 * @author 792793182@qq.com 2015-06-11
 */
abstract class BaseActivity : AppCompatActivity(), BaseView.NetworkChangeListener {

    val contentView: BaseView by lazy { BaseView(this) }

    //提示信息弹出层
    lateinit var toastLayout: ToastLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.run {
            //设置颜色编码格式，否则有些手机会出现颜色阶梯
            setFormat(PixelFormat.RGBA_8888)

            //设置背景颜色
            setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }

        try {
            //去掉默认是标题，自己定义标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        contentView.setNetworkChangeListener(this)
        contentView.headBarLayout.apply {
            setLeftViewStrategy(BaseUIConfig.leftBtn)
            getLeftBtnClickObservable().autoDisposable(disposeOnDestroy()).subscribe { onLeftBtnClick() }
        }

        super.setContentView(contentView)

        toastLayout = ToastLayout(this)
        super.addContentView(toastLayout, getToastLayoutParams())
    }

    open fun onLeftBtnClick() {
        finish()
    }

    /**
     * 网络变化的回调
     *
     * @param isNetworkAvailable 网络是否可用
     */
    override fun onNetworkChange(isNetworkAvailable: Boolean) {

    }

    override fun setContentView(@LayoutRes layoutResId: Int) {
        addContentView(layoutResId)
    }

    override fun setContentView(view: View) {
        addContentView(view)
    }

    override fun setContentView(view: View, lp: LayoutParams) {
        contentView.addView(view, lp)
    }

    fun addContentView(@LayoutRes layoutId: Int): View {
        return contentView.addView(layoutId)
    }

    fun addContentView(view: View) {
        contentView.addView(view)
    }

    fun addContentView(view: View, lp: CoordinatorLayout.LayoutParams) {
        contentView.addView(view, lp)
    }

    override fun addContentView(view: View, lp: LayoutParams) {
        contentView.addView(view, lp)
    }

    override fun setTitle(title: CharSequence) {
        contentView.headBarLayout.setTitle(title)
    }

    override fun setTitle(@StringRes titleStringId: Int) {
        contentView.headBarLayout.setTitle(titleStringId)
    }

    fun setTitleTextSize(@FloatRange(from = 0.0) size: Float) {
        contentView.headBarLayout.setTitleTextSize(size)
    }

    fun showToast(text: String, @IntRange(from = 0) remainTime: Long = 3) {
        toastLayout.show(text, remainTime)
    }

    fun showToast(text: String) {
        toastLayout.show(text, 3)
    }

    fun showToastDontDismiss(text: String) {
        toastLayout.show(text, 0)
    }

    fun dismissToast() {
        toastLayout.dismiss()
    }

    fun disposeOnDestroy() = AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)

    open fun getToastLayoutParams() = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.headHeight)
}
