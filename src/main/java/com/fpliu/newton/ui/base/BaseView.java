package com.fpliu.newton.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.support.design.widget.CoordinatorLayout;
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
        setBackgroundColor(BaseUIConfig.getBgColor());

        RelativeLayout container = new RelativeLayout(context);
        addView(container, new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT));

        headView = new RelativeLayout(context);
        headView.setId(R.id.base_view_head);
        headView.setBackgroundDrawable(BaseUIConfig.getHeadBg());
        headView.setVisibility(GONE);
        container.addView(headView, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.getHeadHeight()));

        titleTv = new TextView(context);
        titleTv.setTextColor(BaseUIConfig.getTitleColor());
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, BaseUIConfig.getTitleSizeSp());
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

    public BaseView addViewInBody(View view, RelativeLayout.LayoutParams lp) {
        bodyView.addView(view, lp);
        return this;
    }

    public BaseView addViewInBody(View view) {
        bodyView.addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        return this;
    }

    /**
     * 注意：这几个重载的方法不同互相调用，因为子类可能会重写，会出现不肯想象的结果
     *
     * @param layoutId 布局文件的ID
     */
    public BaseView addViewInBody(int layoutId) {
        bodyView.addView(View.inflate(getContext(), layoutId, null), new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
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

    public final BaseView setHeadBackgroundColor(int color) {
        if (headView != null) {
            headView.setBackgroundColor(color);
        }
        return this;
    }

    public final BaseView setHeadBackgroundResource(int resId) {
        if (headView != null) {
            headView.setBackgroundResource(resId);
        }
        return this;
    }

    public final BaseView setHeadBackgroundDrawable(Drawable drawable) {
        if (headView != null) {
            headView.setBackgroundDrawable(drawable);
        }
        return this;
    }

    public final BaseView setTitle(CharSequence title) {
        titleTv = new TextView(getContext());
        titleTv.setTextColor(Color.WHITE);
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        titleTv.setMaxLines(1);
        titleTv.setEllipsize(TextUtils.TruncateAt.END);
        titleTv.setText(title);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = UIUtil.dip2px(getContext(), 60);
        lp.rightMargin = UIUtil.dip2px(getContext(), 60);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        headView.addView(titleTv, lp);
        headView.setVisibility(VISIBLE);

        return this;
    }

    public final BaseView setTitle(int titleId) {
        try {
            setTitle(getContext().getString(titleId));
        } catch (Exception e) {
            Logger.e(TAG, "setTitle()", e);
        }
        return this;
    }

    public final BaseView setTitleTextSize(float size) {
        titleTv.setTextSize(size);
        return this;
    }

    public final BaseView setTitleTextColor(int color) {
        titleTv.setTextColor(color);
        return this;
    }

    public final BaseView setTitleTextColorRes(int colorRes) {
        titleTv.setTextColor(getResources().getColor(colorRes));
        return this;
    }

    public Observable<View> getLeftBtnClickObservable() {
        return leftView == null ? Observable.empty() : new ViewClickObservable(leftView);
    }

    public Observable<View> getRightBtnClickObservable() {
        return rightView == null ? Observable.empty() : new ViewClickObservable(rightView);
    }

    public BaseView setLeftViewStrategy(IHeadViewStrategy<?> headViewStrategy) {
        if (headViewStrategy == null) {
            if (leftView != null && leftView.getParent() != null) {
                headView.removeView(leftView);
                leftView = null;
            }
        } else {
            View view = headViewStrategy.onCreateView(headView);
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
                rightView = null;
            }
        } else {
            View view = headViewStrategy.onCreateView(headView);
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
            headViewStrategy.onCreateView(headView);
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(final Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                if (networkChangeListener != null) {
                    networkChangeListener.onNetworkChange(UIUtil.isNetworkAvailable(context));
                }
            }
        }
    };

    public interface NetworkChangeListener {
        /**
         * 网络变化的回调
         *
         * @param isNetworkAvailable 网络是否可用
         */
        void onNetworkChange(boolean isNetworkAvailable);
    }

    public void setNetworkChangeListener(NetworkChangeListener networkChangeListener) {
        this.networkChangeListener = networkChangeListener;
    }
}
