package com.fpliu.newton.ui.base;

import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

import com.fpliu.newton.ui.toast.CustomToast;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

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

        //去掉默认是标题，自己定义标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        contentView = new BaseView(this);
        contentView.setId(R.id.base_view);
        contentView.setNetworkChangeListener(this);
        contentView.setLeftViewStrategy(BaseUIConfig.getLeftBtn())
                .getLeftBtnClickObservable()
                .compose(bindToLifecycle())
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

    public BaseView getContentView() {
        return contentView;
    }


    @Override
    public final void setContentView(int layoutResID) {
        setContentView(View.inflate(this, layoutResID, null));
    }

    @Override
    public void addContentView(View view, LayoutParams params) {
        contentView.addContentView(view, params);
    }

    public void addContentView(View view) {
        contentView.addContentView(view);
    }

    public void addContentView(int layoutId) {
        contentView.addContentView(layoutId);
    }

    public void appendViewInBody(View view, RelativeLayout.LayoutParams params) {
        contentView.appendViewInBody(view, params);
    }

    public void appendViewInBody(View view) {
        contentView.appendViewInBody(view);
    }

    public void appendViewInBody(int layoutId) {
        contentView.appendViewInBody(layoutId);
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

    public void showToast(int resId) {
        try {
            showToast(getResources().getString(resId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showToast(String text) {
        if (!isFinishing()) {
            CustomToast.makeText(getApplicationContext(), text, CustomToast.LENGTH_LONG).show(Gravity.CENTER, 0, 0);
        }
    }

    protected final BaseActivity me() {
        return this;
    }
}
