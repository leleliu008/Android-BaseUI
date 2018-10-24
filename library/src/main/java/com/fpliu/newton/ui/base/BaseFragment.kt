package com.fpliu.newton.ui.base

import android.content.Context
import android.os.Bundle
import android.support.annotation.*
import android.support.annotation.IntRange
import android.support.design.widget.CoordinatorLayout
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxCompoundButton
import com.jakewharton.rxbinding2.widget.RxTextView
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.components.support.RxFragment
import io.reactivex.Observable
import io.reactivex.functions.Consumer

/**
 * Fragment界面基类
 *
 * @author 792793182@qq.com 2015-06-11
 */
abstract class BaseFragment : RxFragment(), BaseView.NetworkChangeListener {

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
                    .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
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

    protected fun text(@IdRes textViewId: Int, text: CharSequence) {
        (contentView.findViewById(textViewId) as? TextView)?.text = text
    }

    protected fun text(textView: TextView, text: CharSequence) {
        textView.text = text
    }

    protected fun text(@IdRes textViewId: Int, @StringRes stringId: Int) {
        (contentView.findViewById(textViewId) as? TextView)?.text = resources.getString(stringId)
    }

    protected fun text(textView: TextView, @StringRes stringId: Int) {
        textView.text = resources.getString(stringId)
    }

    protected fun click(@IdRes viewId: Int): Observable<out View> {
        return ViewClickObservable(contentView.findViewById(viewId)).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
    }

    protected fun click(view: View): Observable<out View> {
        return ViewClickObservable(view).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
    }

    protected fun checkedThenEnabled(compoundButton: CompoundButton, view: View) {
        RxCompoundButton.checkedChanges(compoundButton)
            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            .subscribe(RxView.enabled(view))
    }

    protected fun checkedThenEnabled(@IdRes compoundButtonId: Int, @IdRes viewId: Int) {
        val view1: View = contentView.findViewById(compoundButtonId) ?: return
        val view2: View = contentView.findViewById(viewId) ?: return
        if (view1 is CompoundButton) {
            RxCompoundButton.checkedChanges(view1)
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(RxView.enabled(view2))
        }
    }

    protected fun checkedThenEnabled(compoundButton: CompoundButton, @IdRes viewId: Int) {
        val view: View = contentView.findViewById(viewId) ?: return
        RxCompoundButton.checkedChanges(compoundButton)
            .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
            .subscribe(RxView.enabled(view))
    }

    protected fun checkedThenEnabled(@IdRes compoundButtonId: Int, view: View) {
        val view1: View = contentView.findViewById(compoundButtonId) ?: return
        if (view1 is CompoundButton) {
            RxCompoundButton.checkedChanges(view1)
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(RxView.enabled(view))
        }
    }

    protected fun notEmptyThenEnabled(textView: TextView, view: View) {
        afterTextChange(textView)
            .map { !TextUtils.isEmpty(it) }
            .subscribe(RxView.enabled(view))
    }

    protected fun notEmptyThenEnabled(@IdRes textViewId: Int, @IdRes viewId: Int) {
        val view1: View = contentView.findViewById(textViewId) ?: return
        val view2: View = contentView.findViewById(viewId) ?: return
        if (view1 is TextView) {
            afterTextChange(view1)
                .map { !TextUtils.isEmpty(it) }
                .subscribe(RxView.enabled(view2))
        }
    }

    protected fun notEmptyThenEnabled(textView: TextView, @IdRes viewId: Int) {
        val view: View = contentView.findViewById(viewId) ?: return
        afterTextChange(textView)
            .map { !TextUtils.isEmpty(it) }
            .subscribe(RxView.enabled(view))
    }

    protected fun notEmptyThenEnabled(@IdRes textViewId: Int, view: View) {
        val view1: View = contentView.findViewById(textViewId) ?: return
        if (view1 is TextView) {
            afterTextChange(view1)
                .map { !TextUtils.isEmpty(it) }
                .subscribe(RxView.enabled(view))
        }
    }

    protected fun afterTextChange(textView: TextView): Observable<String> {
        return RxTextView.afterTextChangeEvents(textView).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW)).map { event ->
            event.editable()?.toString() ?: ""
        }
    }

    protected fun afterTextChange2(@IdRes textViewId: Int): Observable<String> {
        return RxTextView.afterTextChangeEvents(contentView.findViewById(textViewId) as TextView).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW)).map { event ->
            event.editable()?.toString() ?: ""
        }
    }

    protected fun editorActions(textView: TextView): Observable<Int> {
        return RxTextView.editorActions(textView).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
    }

    protected fun editorActions(@IdRes textViewId: Int): Observable<Int> {
        return RxTextView.editorActions(contentView.findViewById(textViewId) as TextView).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
    }

    protected fun checkedChange(compoundButton: CompoundButton): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(compoundButton).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
    }

    protected fun checkedChange(@IdRes textViewId: Int): Observable<Boolean> {
        return RxCompoundButton.checkedChanges(contentView.findViewById(textViewId) as CompoundButton).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
    }

    protected fun checked(compoundButton: CompoundButton): Consumer<in Boolean> {
        return RxCompoundButton.checked(compoundButton)
    }

    protected fun checked(@IdRes compoundButtonId: Int): Consumer<in Boolean> {
        return RxCompoundButton.checked(contentView.findViewById(compoundButtonId) as CompoundButton)
    }

    protected fun hint(textView: TextView): Consumer<in CharSequence> {
        return RxTextView.hint(textView)
    }

    protected fun hint(@IdRes textViewId: Int): Consumer<in CharSequence> {
        return RxTextView.hint(contentView.findViewById(textViewId) as TextView)
    }

    protected fun enabled(view: View): Consumer<in Boolean> {
        return RxView.enabled(view)
    }

    protected fun enabled(@IdRes viewId: Int): Consumer<in Boolean> {
        return RxView.enabled(contentView.findViewById(viewId))
    }
}
