package com.fpliu.newton.ui.base

import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.*
import android.support.annotation.IntRange
import android.support.design.widget.CoordinatorLayout
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.Window
import android.widget.CompoundButton
import android.widget.TextView

import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Activity界面基类
 *
 * @author 792793182@qq.com 2015-06-11
 */
abstract class BaseActivity : RxAppCompatActivity(), BaseView.NetworkChangeListener {

    protected val contentView: BaseView by lazy { BaseView(this) }

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
        contentView.headBarLayout.run {
            setLeftViewStrategy(BaseUIConfig.leftBtn)
            getLeftBtnClickObservable()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe { onLeftBtnClick() }
        }

        super.setContentView(contentView)
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
        contentView.showToast(text, remainTime)
    }

    fun showToast(text: String) {
        contentView.showToast(text, 3)
    }

    fun showToastDontDismiss(text: String) {
        contentView.showToastDontDismiss(text)
    }

    fun dismissToast() {
        contentView.dismissToast()
    }

    protected fun me(): BaseActivity {
        return this
    }

    protected fun text(@IdRes textViewId: Int, text: CharSequence) {
        (findViewById(textViewId) as? TextView)?.text = text
    }

    protected fun text(textView: TextView, text: CharSequence) {
        textView.text = text
    }

    protected fun text(@IdRes textViewId: Int, @StringRes stringId: Int) {
        (findViewById(textViewId) as? TextView)?.text = resources.getString(stringId)
    }

    protected fun text(textView: TextView, @StringRes stringId: Int) {
        textView.text = resources.getString(stringId)
    }

    protected fun click(@IdRes viewId: Int): Observable<out View> {
        return ViewClickObservable(findViewById(viewId)).compose(bindUntilEvent(ActivityEvent.DESTROY))
    }

    protected fun click(view: View): Observable<out View> {
        return ViewClickObservable(view).compose(bindUntilEvent(ActivityEvent.DESTROY))
    }

    protected fun checkedThenEnabled(compoundButton: CompoundButton, view: View) {
        RxCompoundButton.checkedChanges(compoundButton)
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(RxView.enabled(view))
    }

    protected fun checkedThenEnabled(@IdRes compoundButtonId: Int, @IdRes viewId: Int) {
        val view1 = findViewById(compoundButtonId) ?: return
        val view2 = findViewById(viewId) ?: return
        if (view1 is CompoundButton) {
            RxCompoundButton.checkedChanges(view1)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(RxView.enabled(view2))
        }
    }

    protected fun checkedThenEnabled(compoundButton: CompoundButton, @IdRes viewId: Int) {
        val view = findViewById(viewId) ?: return
        RxCompoundButton.checkedChanges(compoundButton)
            .compose(bindUntilEvent(ActivityEvent.DESTROY))
            .subscribe(RxView.enabled(view))
    }

    protected fun checkedThenEnabled(@IdRes compoundButtonId: Int, view: View) {
        val view1 = findViewById(compoundButtonId) ?: return
        if (view1 is CompoundButton) {
            RxCompoundButton.checkedChanges(view1)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(RxView.enabled(view))
        }
    }

    protected fun notEmptyThenEnabled(textView: TextView, view: View) {
        afterTextChange(textView)
            .map { !TextUtils.isEmpty(it) }
            .subscribe(RxView.enabled(view))
    }

    protected fun notEmptyThenEnabled(@IdRes textViewId: Int, @IdRes viewId: Int) {
        val view1 = findViewById(textViewId) ?: return
        val view2 = findViewById(viewId) ?: return
        if (view1 is TextView) {
            afterTextChange(view1)
                .map { !TextUtils.isEmpty(it) }
                .subscribe(RxView.enabled(view2))
        }
    }

    protected fun notEmptyThenEnabled(textView: TextView, @IdRes viewId: Int) {
        val view = findViewById(viewId) ?: return
        afterTextChange(textView)
            .map { !TextUtils.isEmpty(it) }
            .subscribe(RxView.enabled(view))
    }

    protected fun notEmptyThenEnabled(@IdRes textViewId: Int, view: View) {
        val view1 = findViewById(textViewId) ?: return
        if (view1 is TextView) {
            afterTextChange(view1)
                .map { !TextUtils.isEmpty(it) }
                .subscribe(RxView.enabled(view))
        }
    }

    protected fun afterTextChange(textView: TextView): Observable<String> {
        return RxTextView.afterTextChangeEvents(textView).compose(bindUntilEvent(ActivityEvent.DESTROY)).map { event ->
            event.editable()?.toString() ?: ""
        }
    }

    protected fun afterTextChange(textViewId: Int): Observable<String> {
        return RxTextView.afterTextChangeEvents(findViewById(textViewId) as TextView).compose(bindUntilEvent(ActivityEvent.DESTROY)).map { event ->
            event.editable()?.toString() ?: ""
        }
    }

    protected fun editorActions(textView: TextView): Observable<Int> {
        return RxTextView.editorActions(textView).compose(bindUntilEvent(ActivityEvent.DESTROY))
    }

    protected fun editorActions(textViewId: Int): Observable<Int> {
        return RxTextView.editorActions(findViewById(textViewId) as TextView).compose(bindUntilEvent(ActivityEvent.DESTROY))
    }

    protected fun checkedChange(compoundButton: CompoundButton): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(compoundButton).compose(bindUntilEvent(ActivityEvent.DESTROY))
    }

    protected fun checkedChange(textViewId: Int): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(findViewById(textViewId) as CompoundButton).compose(bindUntilEvent(ActivityEvent.DESTROY))
    }

    protected fun checked(compoundButton: CompoundButton): Consumer<in Boolean> {
        return RxCompoundButton.checked(compoundButton)
    }

    protected fun checked(compoundButtonId: Int): Consumer<in Boolean> {
        return RxCompoundButton.checked(findViewById(compoundButtonId) as CompoundButton)
    }

    protected fun hint(textView: TextView): Consumer<in CharSequence> {
        return RxTextView.hint(textView)
    }

    protected fun hint(textViewId: Int): Consumer<in CharSequence> {
        return RxTextView.hint(findViewById(textViewId) as TextView)
    }

    protected fun enabled(view: View): Consumer<in Boolean> {
        return RxView.enabled(view)
    }

    protected fun enabled(viewId: Int): Consumer<in Boolean> {
        return RxView.enabled(findViewById(viewId))
    }
}
