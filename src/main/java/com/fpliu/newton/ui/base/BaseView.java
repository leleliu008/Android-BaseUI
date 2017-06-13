package com.fpliu.newton.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fpliu.newton.log.Logger;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observable;

/**
 * 视图基类
 *
 * @author 792793182@qq.com 2015-09-18.
 */
public class BaseView extends CoordinatorLayout {

    private static final String TAG = BaseView.class.getSimpleName();

    /**
     * 标题栏文本
     */
    private TextView titleTv;

    /**
     * 标题栏左边的按钮
     */
    private View leftView;

    /**
     * 标题栏右边的按钮
     */
    private View rightView;

    /**
     * 标题栏
     */
    private RelativeLayout headView;

    /**
     * 标题栏下面的视图
     */
    private RelativeLayout bodyView;

    private NetworkChangeListener networkChangeListener;

    private boolean isNetworkAvailable = true;

    public BaseView(Context context) {
        super(context);
        init(context);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(GlobalConfig.getBgColor());

        RelativeLayout container = new RelativeLayout(context);
        addView(container, new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));

        headView = new RelativeLayout(context);
        headView.setId(R.id.base_view_head);
        headView.setBackgroundColor(GlobalConfig.getHeadBgColor());
        container.addView(headView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, GlobalConfig.getHeadHeight()));

        titleTv = new TextView(context);
        titleTv.setTextColor(Color.WHITE);
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleTv.setGravity(Gravity.CENTER);
        titleTv.setMaxLines(1);
        titleTv.setEllipsize(TextUtils.TruncateAt.END);
        headView.addView(titleTv, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));

        bodyView = new RelativeLayout(context);
        bodyView.setId(R.id.base_view_body);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        lp.addRule(RelativeLayout.BELOW, R.id.base_view_head);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        container.addView(bodyView, lp);
    }

    public BaseView addContentView(View view, ViewGroup.LayoutParams params) {
        bodyView.addView(view, params);
        return this;
    }

    public BaseView addContentView(View view) {
        bodyView.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return this;
    }

    /**
     * 注意：这几个重载的方法不同互相调用，因为子类可能会重写，会出现不肯想象的结果
     *
     * @param layoutId 布局文件的ID
     */
    public BaseView addContentView(int layoutId) {
        bodyView.addView(View.inflate(getContext(), layoutId, null), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return this;
    }

    public BaseView appendViewInBody(View view, RelativeLayout.LayoutParams params) {
        bodyView.addView(view, params);
        return this;
    }

    public BaseView appendViewInBody(View view) {
        bodyView.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return this;
    }

    /**
     * 注意：这几个重载的方法不同互相调用，因为子类可能会重写，会出现不肯想象的结果
     *
     * @param layoutId 布局文件的ID
     */
    public BaseView appendViewInBody(int layoutId) {
        bodyView.addView(View.inflate(getContext(), layoutId, null), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        return this;
    }

    /**
     * 隐藏掉头部
     */
    public final boolean isHeadViewShowing() {
        return headView == null ? false : headView.getVisibility() == VISIBLE;
    }

    /**
     * 隐藏掉头部
     */
    public final BaseView hideHeadView() {
        headView.setVisibility(View.GONE);
        return this;
    }

    /**
     * 显示头部
     */
    public final BaseView showHeadView() {
        headView.setVisibility(View.VISIBLE);
        return this;
    }

    public final BaseView setHeadView(View view) {
        headView.removeAllViews();
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        headView.addView(view, lp1);
        return this;
    }

    public final BaseView setHeadView(int layoutId) {
        headView.removeAllViews();
        LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        headView.addView(LayoutInflater.from(getContext()).inflate(layoutId, headView, false), lp1);
        return this;
    }

    public final BaseView setTitle(CharSequence title) {
        if (titleTv == null) {
            titleTv = new TextView(getContext());
            titleTv.setTextColor(Color.WHITE);
            titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            titleTv.setMaxLines(1);
            titleTv.setEllipsize(TextUtils.TruncateAt.END);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.leftMargin = UIUtil.dip2px(getContext(), 60);
            lp.rightMargin = UIUtil.dip2px(getContext(), 60);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            headView.addView(titleTv, lp);
        }
        titleTv.setText(title);
        return this;
    }

    public final BaseView setTitle(int titleId) {
        String title = "";
        try {
            title = getContext().getString(titleId);
            setTitle(title);
        } catch (Exception e) {
            Logger.e(TAG, "setTitle()", e);
        }
        return this;
    }

    public final BaseView setTitleTextSize(float size) {
        if (titleTv == null) {
            setTitle("");
        }
        titleTv.setTextSize(size);
        return this;
    }

    public Observable<Object> getLeftBtnClickObservable() {
        return leftView == null ? Observable.empty() : RxView.clicks(leftView);
    }

    public Observable<Object> getRightBtnClickObservable() {
        return rightView == null ? Observable.empty() : RxView.clicks(rightView);
    }

    public BaseView setLeftViewStrategy(IHeadViewStrategy<?> headViewStrategy) {
        if (headViewStrategy == null) {
            if (leftView != null && leftView.getParent() != null) {
                headView.removeView(leftView);
            }
        } else {
            View view = headViewStrategy.getView(headView);
            if (view != null && view.getParent() == null) {
                if (leftView != null && leftView.getParent() != null) {
                    headView.removeView(leftView);
                }

                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                headView.addView(view, lp);
                leftView = view;
            }
        }
        return this;
    }

    public BaseView setRightViewStrategy(IHeadViewStrategy<?> headViewStrategy) {
        if (headViewStrategy == null) {
            if (rightView != null && rightView.getParent() != null) {
                headView.removeView(rightView);
            }
        } else {
            View view = headViewStrategy.getView(headView);
            if (view != null && view.getParent() == null) {
                if (rightView != null && rightView.getParent() != null) {
                    headView.removeView(rightView);
                }
                RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                headView.addView(view, lp);
                rightView = view;
            }
        }
        return this;
    }

    public final BaseView addHeadViewStrategy(IHeadViewStrategy<?> headViewStrategy) {
        if (headViewStrategy != null) {
            headViewStrategy.getView(headView);
        }
        return this;
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        //注册网络变化的监听器
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(receiver, intentFilter);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        try {
            getContext().unregisterReceiver(receiver);
        } catch (Exception e) {
            Logger.e(TAG, "onDetachedFromWindow()", e);
        }
        receiver = null;
    }

    public Snackbar showSnackBar(String message, int duration) {
        final Snackbar snackbar = Snackbar.make(this, message, duration);

        //防止崩溃掉
        try {
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
        } catch (Exception e) {
            // do nothing
        }

        snackbar.setActionTextColor(getResources().getColor(GlobalConfig.getHeadBgColor()));
        snackbar.setAction("确定", v -> snackbar.dismiss());
        snackbar.show();
        return snackbar;
    }

    public final Snackbar showSnackBarWithAction(String message, int duration, String actionText, View.OnClickListener onClickListener) {
        final Snackbar snackbar = Snackbar.make(this, message, duration);

        //防止崩溃掉
        try {
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
        } catch (Exception e) {
            // do nothing
        }

        snackbar.setAction(actionText, onClickListener);
        snackbar.show();
        return snackbar;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean needContinue = true;
                boolean isNetworkAvailable = UIUtil.isNetworkAvailable(context);
                if (networkChangeListener != null) {
                    needContinue = networkChangeListener.onNetworkChange(isNetworkAvailable);
                }

                if (needContinue) {
                    if (!BaseView.this.isNetworkAvailable) {
                        if (isNetworkAvailable) {
                            showSnackBar("网络已连接", Snackbar.LENGTH_LONG);
                        } else {
                            showSnackBarWithAction("网络未连接", Snackbar.LENGTH_INDEFINITE, "设置", v -> UIUtil.startNetSettingActivity(context));
                        }
                    } else {
                        if (!isNetworkAvailable) {
                            showSnackBarWithAction("网络未连接", Snackbar.LENGTH_INDEFINITE, "设置", v -> UIUtil.startNetSettingActivity(context));
                        }
                    }
                }

                BaseView.this.isNetworkAvailable = isNetworkAvailable;
            }
        }
    };

    public interface NetworkChangeListener {
        /**
         * 网络变化的回调
         *
         * @param isNetworkAvailable 网络是否可用
         */
        boolean onNetworkChange(boolean isNetworkAvailable);
    }

    public void setNetworkChangeListener(NetworkChangeListener networkChangeListener) {
        this.networkChangeListener = networkChangeListener;
    }
}
