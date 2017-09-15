package com.fpliu.newton.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fpliu.newton.log.Logger;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Fragment界面基类
 *
 * @author 792793182@qq.com 2015-06-11
 */
public abstract class BaseFragment extends RxFragment implements BaseView.NetworkChangeListener {

    private BaseView contentView;

    @Override
    public BaseView onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = new BaseView(getActivity());
        contentView.setId(R.id.base_view);
        contentView.setNetworkChangeListener(this);
        contentView.setLeftViewStrategy(BaseUIConfig.getLeftBtn())
                .getLeftBtnClickObservable()
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(o -> onLeftBtnClick());
        return contentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        contentView = null;
    }

    public void onLeftBtnClick() {
        getActivity().onBackPressed();
    }

    /**
     * 网络变化的回调
     *
     * @param isNetworkAvailable 网络是否可用
     */
    @Override
    public void onNetworkChange(boolean isNetworkAvailable) {

    }

    public final BaseView getContentView() {
        return contentView;
    }

    public final void addViewInBody(View view, RelativeLayout.LayoutParams params) {
        if (contentView == null) {
            return;
        }
        contentView.addViewInBody(view, params);
    }

    public final void addViewInBody(View view) {
        if (contentView == null) {
            return;
        }
        contentView.addViewInBody(view);
    }

    public final void addViewInBody(int layoutId) {
        if (contentView == null) {
            return;
        }
        contentView.addViewInBody(layoutId);
    }

    /**
     * 隐藏掉头部
     */
    public final void hideHeadView() {
        if (contentView == null) {
            return;
        }
        contentView.hideHeadView();
    }

    /**
     * 替换掉头部
     */
    public final void setHeadView(View view) {
        if (contentView == null) {
            return;
        }
        contentView.setHeadView(view);
    }

    public final void setTitle(CharSequence title) {
        if (contentView != null) {
            contentView.setTitle(title);
        }
    }

    public final void setTitle(int titleId) {
        if (contentView != null) {
            contentView.setTitle(titleId);
        }
    }

    public final void setTitleTextSize(float size) {
        if (contentView != null) {
            contentView.setTitleTextSize(size);
        }
    }

    public final void showToast(CharSequence text) {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            Toast.makeText(activity, text, Toast.LENGTH_LONG).show();
        }
    }

    public final void showToast(int resId) {
        try {
            showToast(getResources().getString(resId));
        } catch (Exception e) {
            Logger.e(BaseFragment.class.getSimpleName(), "showToast()", e);
        }
    }

    protected final void text(int textViewId, CharSequence text) {
        View view = contentView.findViewById(textViewId);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
        }
    }

    protected final void text(TextView textView, CharSequence text) {
        if (textView != null) {
            textView.setText(text);
        }
    }

    protected final void text(int textViewId, int stringId) {
        View view = contentView.findViewById(textViewId);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setText(getResources().getString(stringId));
            }
        }
    }

    protected final void text(TextView textView, int stringId) {
        if (textView != null) {
            textView.setText(getResources().getString(stringId));
        }
    }

    protected final Observable<? extends View> click(int textViewId) {
        return new ViewClickObservable(contentView.findViewById(textViewId)).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }

    protected final Observable<? extends View> click(View view) {
        return new ViewClickObservable(view).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }

    protected final void checkedThenEnabled(CompoundButton compoundButton, View view) {
        if (compoundButton == null || view == null) {
            return;
        }
        RxCompoundButton.checkedChanges(compoundButton)
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(RxView.enabled(view));
    }

    protected final void checkedThenEnabled(int compoundButtonId, int viewId) {
        View view1 = contentView.findViewById(compoundButtonId);
        View view2 = contentView.findViewById(viewId);
        if (view1 == null || view2 == null) {
            return;
        }
        if (view1 instanceof CompoundButton) {
            RxCompoundButton.checkedChanges((CompoundButton) view1)
                    .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(RxView.enabled(view2));
        }
    }

    protected final void checkedThenEnabled(CompoundButton compoundButton, int viewId) {
        View view = contentView.findViewById(viewId);
        if (compoundButton == null || view == null) {
            return;
        }
        RxCompoundButton.checkedChanges(compoundButton)
                .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                .subscribe(RxView.enabled(view));
    }

    protected final void checkedThenEnabled(int compoundButtonId, View view) {
        View view1 = contentView.findViewById(compoundButtonId);
        if (view1 == null || view == null) {
            return;
        }
        if (view1 instanceof CompoundButton) {
            RxCompoundButton.checkedChanges((CompoundButton) view1)
                    .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
                    .subscribe(RxView.enabled(view));
        }
    }

    protected final void notEmptyThenEnabled(TextView textView, View view) {
        afterTextChange(textView)
                .map(text -> !TextUtils.isEmpty(text))
                .subscribe(RxView.enabled(view));
    }

    protected final void notEmptyThenEnabled(int textViewId, int viewId) {
        View view1 = contentView.findViewById(textViewId);
        View view2 = contentView.findViewById(viewId);
        if (view1 == null || view2 == null) {
            return;
        }
        if (view1 instanceof TextView) {
            afterTextChange((TextView) view1)
                    .map(text -> !TextUtils.isEmpty(text))
                    .subscribe(RxView.enabled(view2));
        }
    }

    protected final void notEmptyThenEnabled(TextView textView, int viewId) {
        View view = contentView.findViewById(viewId);
        if (textView == null || view == null) {
            return;
        }
        afterTextChange(textView)
                .map(text -> !TextUtils.isEmpty(text))
                .subscribe(RxView.enabled(view));
    }

    protected final void notEmptyThenEnabled(int textViewId, View view) {
        View view1 = contentView.findViewById(textViewId);
        if (view1 == null || view == null) {
            return;
        }
        if (view1 instanceof TextView) {
            afterTextChange((TextView) view1)
                    .map(text -> !TextUtils.isEmpty(text))
                    .subscribe(RxView.enabled(view));
        }
    }

    protected final Observable<String> afterTextChange(TextView textView) {
        return RxTextView.afterTextChangeEvents(textView).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW)).map(event -> event.editable().toString());
    }

    protected final Observable<String> afterTextChange(int textViewId) {
        return RxTextView.afterTextChangeEvents((TextView) contentView.findViewById(textViewId)).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW)).map(event -> event.editable().toString());
    }

    protected final Observable<Integer> editorActions(TextView textView) {
        return RxTextView.editorActions(textView).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }

    protected final Observable<Integer> editorActions(int textViewId) {
        return RxTextView.editorActions((TextView) contentView.findViewById(textViewId)).compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW));
    }

    protected final Consumer<? super CharSequence> hint(TextView textView) {
        return RxTextView.hint(textView);
    }

    protected final Consumer<? super CharSequence> hint(int textViewId) {
        return RxTextView.hint((TextView) contentView.findViewById(textViewId));
    }

    protected final Consumer<? super Boolean> enabled(View view) {
        return RxView.enabled(view);
    }

    protected final Consumer<? super Boolean> enabled(int viewId) {
        return RxView.enabled(contentView.findViewById(viewId));
    }
}
