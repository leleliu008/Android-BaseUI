package com.fpliu.newton.ui.base;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fpliu.newton.log.Logger;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxCompoundButton;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Activity界面基类
 *
 * @author 792793182@qq.com 2015-06-11
 */
public abstract class BaseActivity extends RxAppCompatActivity implements BaseView.NetworkChangeListener {

    private BaseView contentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Window window = getWindow();

        //设置颜色编码格式，否则有些手机会出现颜色阶梯
        window.setFormat(PixelFormat.RGBA_8888);

        //设置背景颜色
        window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        try {
            //去掉默认是标题，自己定义标题栏
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        contentView = new BaseView(this);
        contentView.setId(R.id.base_view);
        contentView.setNetworkChangeListener(this);
        contentView.setLeftViewStrategy(BaseUIConfig.getLeftBtn())
                .getLeftBtnClickObservable()
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(o -> onLeftBtnClick());
        setContentView(contentView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        contentView = null;
    }

    public void onLeftBtnClick() {
        finish();
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


    @Override
    public final void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    public final void addContentView(View view) {
        addContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public final void addContentView(int layoutId) {
        addContentView(View.inflate(this, layoutId, null), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public final void addViewInBody(View view, RelativeLayout.LayoutParams params) {
        contentView.addViewInBody(view, params);
    }

    public final void addViewInBody(View view) {
        contentView.addViewInBody(view);
    }

    public final void addViewInBody(int layoutId) {
        contentView.addViewInBody(layoutId);
    }

    @Override
    public final void setTitle(CharSequence title) {
        if (contentView != null) {
            contentView.setTitle(title);
        }
    }

    @Override
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

    public final void showToast(int resId) {
        try {
            showToast(getResources().getString(resId));
        } catch (Exception e) {
            Logger.e(getClass().getSimpleName(), "showToast()", e);
        }
    }

    public final void showToast(CharSequence text) {
        if (!isFinishing()) {
            UIUtil.makeToast(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
    }

    protected final BaseActivity me() {
        return this;
    }

    protected final void text(int textViewId, CharSequence text) {
        View view = findViewById(textViewId);
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
        View view = findViewById(textViewId);
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
        return new ViewClickObservable(findViewById(textViewId)).compose(bindUntilEvent(ActivityEvent.DESTROY));
    }

    protected final Observable<? extends View> click(View view) {
        return new ViewClickObservable(view).compose(bindUntilEvent(ActivityEvent.DESTROY));
    }

    protected final void checkedThenEnabled(CompoundButton compoundButton, View view) {
        if (compoundButton == null || view == null) {
            return;
        }
        RxCompoundButton.checkedChanges(compoundButton)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(RxView.enabled(view));
    }

    protected final void checkedThenEnabled(int compoundButtonId, int viewId) {
        View view1 = findViewById(compoundButtonId);
        View view2 = findViewById(viewId);
        if (view1 == null || view2 == null) {
            return;
        }
        if (view1 instanceof CompoundButton) {
            RxCompoundButton.checkedChanges((CompoundButton) view1)
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(RxView.enabled(view2));
        }
    }

    protected final void checkedThenEnabled(CompoundButton compoundButton, int viewId) {
        View view = findViewById(viewId);
        if (compoundButton == null || view == null) {
            return;
        }
        RxCompoundButton.checkedChanges(compoundButton)
                .compose(bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(RxView.enabled(view));
    }

    protected final void checkedThenEnabled(int compoundButtonId, View view) {
        View view1 = findViewById(compoundButtonId);
        if (view1 == null || view == null) {
            return;
        }
        if (view1 instanceof CompoundButton) {
            RxCompoundButton.checkedChanges((CompoundButton) view1)
                    .compose(bindUntilEvent(ActivityEvent.DESTROY))
                    .subscribe(RxView.enabled(view));
        }
    }

    protected final void notEmptyThenEnabled(TextView textView, View view) {
        afterTextChange(textView)
                .map(text -> !TextUtils.isEmpty(text))
                .subscribe(RxView.enabled(view));
    }

    protected final void notEmptyThenEnabled(int textViewId, int viewId) {
        View view1 = findViewById(textViewId);
        View view2 = findViewById(viewId);
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
        View view = findViewById(viewId);
        if (textView == null || view == null) {
            return;
        }
        afterTextChange(textView)
                .map(text -> !TextUtils.isEmpty(text))
                .subscribe(RxView.enabled(view));
    }

    protected final void notEmptyThenEnabled(int textViewId, View view) {
        View view1 = findViewById(textViewId);
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
        return RxTextView.afterTextChangeEvents(textView).compose(bindUntilEvent(ActivityEvent.DESTROY)).map(event -> event.editable().toString());
    }

    protected final Observable<String> afterTextChange(int textViewId) {
        return RxTextView.afterTextChangeEvents((TextView) findViewById(textViewId)).compose(bindUntilEvent(ActivityEvent.DESTROY)).map(event -> event.editable().toString());
    }

    protected final Observable<Integer> editorActions(TextView textView) {
        return RxTextView.editorActions(textView).compose(bindUntilEvent(ActivityEvent.DESTROY));
    }

    protected final Observable<Integer> editorActions(int textViewId) {
        return RxTextView.editorActions((TextView) findViewById(textViewId)).compose(bindUntilEvent(ActivityEvent.DESTROY));
    }

    protected final Consumer<? super CharSequence> hint(TextView textView) {
        return RxTextView.hint(textView);
    }

    protected final Consumer<? super CharSequence> hint(int textViewId) {
        return RxTextView.hint((TextView) findViewById(textViewId));
    }

    protected final Consumer<? super Boolean> enabled(View view) {
        return RxView.enabled(view);
    }

    protected final Consumer<? super Boolean> enabled(int viewId) {
        return RxView.enabled(findViewById(viewId));
    }
}
