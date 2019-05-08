package com.fpliu.newton.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.FloatRange
import androidx.annotation.IntRange
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider
import com.uber.autodispose.autoDisposable

/**
 * Fragment界面基类
 *
 * @author 792793182@qq.com 2015-06-11
 */
abstract class BaseFragment : Fragment(), BaseView.NetworkChangeListener {

    protected lateinit var contentView: BaseView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentView = BaseView(activity as Context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): BaseView? {
        return contentView.apply {
            setNetworkChangeListener(this@BaseFragment)
            headBarLayout.apply {
                setLeftViewStrategy(BaseUIConfig.leftBtn)
                getLeftBtnClickObservable()
                    .autoDisposable(disposeOnDestroy())
                    .subscribe { onLeftBtnClick() }
            }
        }
    }

    open fun onLeftBtnClick() {
        activity?.onBackPressed()
    }

    /**
     * 网络变化的回调
     *
     * @param isNetworkAvailable 网络是否可用
     */
    override fun onNetworkChange(isNetworkAvailable: Boolean) {

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

    fun setTitle(title: CharSequence) {
        contentView.headBarLayout.setTitle(title)
    }

    fun setTitle(@StringRes titleStringId: Int) {
        contentView.headBarLayout.setTitle(titleStringId)
    }

    fun setTitleTextSize(@FloatRange(from = 0.0) size: Float) {
        contentView.headBarLayout.setTitleTextSize(size)
    }

    fun showToast(text: String, @IntRange(from = 0) remainTime: Long = 3) {
        val activity = activity ?: return
        if (activity is BaseActivity) {
            activity.showToast(text, remainTime)
        } else {
            UIUtil.makeToast(activity, text, Toast.LENGTH_LONG).show()
        }
    }

    fun showToast(text: String) {
        showToast(text, 3)
    }

    fun showToastDontDismiss(text: String) {
        showToast(text, 0)
    }

    fun dismissToast() {
        val activity = activity ?: return
        if (activity is BaseActivity) {
            activity.dismissToast()
        }
    }

    fun disposeOnDestroy() = AndroidLifecycleScopeProvider.from(this, Lifecycle.Event.ON_DESTROY)

}
