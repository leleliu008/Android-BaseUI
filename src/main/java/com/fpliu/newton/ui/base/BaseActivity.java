package com.fpliu.newton.ui.base;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fpliu.newton.log.Logger;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import io.reactivex.Observable;

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

    public final void showToast(String text) {
        if (!isFinishing()) {
            UIUtil.makeToast(getApplicationContext(), text, Toast.LENGTH_LONG).show();
        }
    }

    protected final BaseActivity me() {
        return this;
    }

    protected final void text(int textViewId, String text) {
        View view = findViewById(textViewId);
        if (view != null) {
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
        }
    }

    protected final void text(TextView textView, String text) {
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
}
