package com.fpliu.newton.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
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

    private ToastLayout toastLayout;

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

        AppBarLayout appBarLayout = new AppBarLayout(context);
        addView(appBarLayout, new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        headView = new RelativeLayout(context);
        headView.setId(R.id.base_view_head);
        headView.setBackgroundDrawable(BaseUIConfig.getHeadBg());
        headView.setVisibility(GONE);
        AppBarLayout.LayoutParams lp2 = new AppBarLayout.LayoutParams(AppBarLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.getHeadHeight());
        lp2.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
        appBarLayout.addView(headView, lp2);

        titleTv = new TextView(context);
        titleTv.setTextColor(BaseUIConfig.getTitleColor());
        titleTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, BaseUIConfig.getTitleSizeSp());
        titleTv.setGravity(Gravity.CENTER);
        titleTv.setMaxLines(1);
        titleTv.setEllipsize(TextUtils.TruncateAt.END);
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.leftMargin = UIUtil.dip2px(getContext(), BaseUIConfig.getTitlePaddingLeftRight());
        lp.rightMargin = UIUtil.dip2px(getContext(), BaseUIConfig.getTitlePaddingLeftRight());
        lp.addRule(RelativeLayout.CENTER_IN_PARENT);
        headView.addView(titleTv, lp);

        toastLayout = new ToastLayout(context);
        addView(toastLayout, new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, BaseUIConfig.getHeadHeight()));
    }

    @Override
    public void addView(View view, ViewGroup.LayoutParams lp) {
        BaseView.LayoutParams lp2;
        if (lp == null) {
            lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        } else {
            lp2 = new LayoutParams(lp.width, lp.height);
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) lp;
                lp2.topMargin = marginLayoutParams.topMargin;
                lp2.leftMargin = marginLayoutParams.leftMargin;
                lp2.rightMargin = marginLayoutParams.rightMargin;
                lp2.bottomMargin = marginLayoutParams.bottomMargin;
                if (lp instanceof FrameLayout.LayoutParams) {
                    lp2.gravity = ((FrameLayout.LayoutParams) lp).gravity;
                } else if (lp instanceof LinearLayout.LayoutParams) {
                    lp2.gravity = ((LinearLayout.LayoutParams) lp).gravity;
                } else if (lp instanceof DrawerLayout.LayoutParams) {
                    lp2.gravity = ((DrawerLayout.LayoutParams) lp).gravity;
                } else if (lp instanceof ViewPager.LayoutParams) {
                    lp2.gravity = ((ViewPager.LayoutParams) lp).gravity;
                }
            }
        }
        super.addView(view, lp2);
    }

    @Override
    public void addView(View view) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        BaseView.LayoutParams lp2;
        if (lp == null) {
            lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        } else {
            lp2 = new LayoutParams(lp.width, lp.height);
            if (lp instanceof MarginLayoutParams) {
                MarginLayoutParams marginLayoutParams = (MarginLayoutParams) lp;
                lp2.topMargin = marginLayoutParams.topMargin;
                lp2.leftMargin = marginLayoutParams.leftMargin;
                lp2.rightMargin = marginLayoutParams.rightMargin;
                lp2.bottomMargin = marginLayoutParams.bottomMargin;
                if (lp instanceof FrameLayout.LayoutParams) {
                    lp2.gravity = ((FrameLayout.LayoutParams) lp).gravity;
                } else if (lp instanceof LinearLayout.LayoutParams) {
                    lp2.gravity = ((LinearLayout.LayoutParams) lp).gravity;
                } else if (lp instanceof DrawerLayout.LayoutParams) {
                    lp2.gravity = ((DrawerLayout.LayoutParams) lp).gravity;
                } else if (lp instanceof ViewPager.LayoutParams) {
                    lp2.gravity = ((ViewPager.LayoutParams) lp).gravity;
                }
            }
        }
        super.addView(view, lp2);
    }

    public BaseView addView(View view, CoordinatorLayout.LayoutParams lp) {
        super.addView(view, lp);
        return this;
    }

    public BaseView addView(View view, int gravity, int xOffset, int yOffset) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        BaseView.LayoutParams lp2;
        if (lp == null) {
            lp2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        } else {
            lp2 = new LayoutParams(lp.width, lp.height);
        }
        lp2.gravity = gravity;
        switch (gravity) {
            case Gravity.LEFT | Gravity.TOP:
                lp2.leftMargin = xOffset;
                lp2.topMargin = yOffset;
                break;
            case Gravity.LEFT | Gravity.BOTTOM:
                lp2.leftMargin = xOffset;
                lp2.bottomMargin = yOffset;
                break;
            case Gravity.RIGHT | Gravity.TOP:
                lp2.rightMargin = xOffset;
                lp2.topMargin = yOffset;
                break;
            case Gravity.RIGHT | Gravity.BOTTOM:
                lp2.rightMargin = xOffset;
                lp2.bottomMargin = yOffset;
                break;
            case Gravity.CENTER:
                lp2.leftMargin = xOffset;
                lp2.topMargin = yOffset;
                break;
        }
        super.addView(view, lp);
        return this;
    }

    /**
     * 注意：这几个重载的方法不同互相调用，因为子类可能会重写，会出现不肯想象的结果
     *
     * @param layoutId 布局文件的ID
     */
    public BaseView addView(int layoutId) {
        View.inflate(getContext(), layoutId, this);
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
        titleTv.setText(title);
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

    public final void showToast(String text, Long remainTime) {
        toastLayout.show(text, remainTime);
    }

    public final void showToast(String text) {
        toastLayout.show(text, 3);
    }

    public final void showToastDontDismiss(String text) {
        toastLayout.show(text, 0);
    }

    public final void dismissToast() {
        toastLayout.dismiss();
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
