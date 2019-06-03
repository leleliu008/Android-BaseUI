package com.fpliu.newton.ui.base

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
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
abstract class BaseActivity : AppCompatActivity() {

    val contentView: BaseView by lazy { BaseView(this) }

    //提示信息弹出层
    lateinit var toastLayout: ToastLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //设置颜色编码格式，否则有些手机会出现颜色阶梯
        window.setFormat(PixelFormat.RGBA_8888)

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

    open fun getToastLayoutParams() = BaseUIConfig.toastLayoutLayoutParams
}
