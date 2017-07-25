package com.fpliu.newton.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.fpliu.newton.log.Logger;
import com.fpliu.newton.ui.toast.CustomToast;
import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * Fragment界面基类
 *
 * @author 792793182@qq.com 2015-06-11
 */
public abstract class BaseFragment extends RxFragment {

    private static final String TAG = BaseFragment.class.getSimpleName();

    private BaseView contentView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //注册网络变化的监听器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getActivity().registerReceiver(receiver, intentFilter);
    }

    @Override
    public BaseView onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = new BaseView(getActivity());
        contentView.setId(R.id.base_view);
        contentView.setLeftViewStrategy(BaseUIConfig.getLeftBtn())
                .getLeftBtnClickObservable()
                .compose(bindToLifecycle())
                .subscribe(o -> onLeftBtnClick());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        try {
            getActivity().unregisterReceiver(receiver);
        } catch (Exception e) {
            Logger.e(TAG, "onDestroy()", e);
        }

        contentView = null;
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

    public void onLeftBtnClick() {
        getActivity().onBackPressed();
    }

    public BaseView getContentView() {
        return contentView;
    }

    /**
     * 网络变化的回调
     *
     * @param isNetworkAvailable 网络是否可用
     */
    protected void onNetworkChange(boolean isNetworkAvailable) {

    }

    public void showToast(String text) {
        CustomToast.makeText(getContext(), text, CustomToast.LENGTH_LONG).show(Gravity.CENTER, 0, 0);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                onNetworkChange(UIUtil.isNetworkAvailable(context));
            }
        }
    };
}
