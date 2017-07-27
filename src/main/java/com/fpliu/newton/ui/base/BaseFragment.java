package com.fpliu.newton.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.fpliu.newton.ui.toast.CustomToast;
import com.trello.rxlifecycle2.components.support.RxFragment;

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
                .compose(bindToLifecycle())
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

    public void addContentView(View view, LayoutParams params) {
        if (contentView == null) {
            return;
        }
        contentView.addContentView(view, params);
    }

    public void addContentView(View view) {
        if (contentView == null) {
            return;
        }
        contentView.addContentView(view);
    }

    public void addContentView(int layoutId) {
        if (contentView == null) {
            return;
        }
        contentView.addContentView(layoutId);
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

    public void showToast(String text) {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing()) {
            CustomToast.makeText(activity, text, CustomToast.LENGTH_LONG).show(Gravity.CENTER, 0, 0);
        }
    }

    public void showToast(int resId) {
        try {
            showToast(getResources().getString(resId));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
