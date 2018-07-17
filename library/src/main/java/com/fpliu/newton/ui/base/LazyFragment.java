package com.fpliu.newton.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * <h1>懒加载Fragment</h1> 只有创建并显示的时候才会调用onCreateViewLazy方法<br>
 * <br>
 * <br>
 * 懒加载的原理onCreateView的时候Fragment有可能没有显示出来。<br>
 * 但是调用到setUserVisibleHint(boolean isVisibleToUser),isVisibleToUser =
 * true的时候就说明有显示出来<br>
 * 但是要考虑onCreateView和setUserVisibleHint的先后问题所以才有了下面的代码
 * <br>
 * 注意：<br>
 * 《1》原先的Fragment的回调方法名字后面要加个Lazy，比如Fragment的onCreateView方法， 就写成onCreateViewLazy <br>
 * 《2》使用该LazyFragment会导致多一层布局深度
 *
 * @author 792793182@qq.com 2016-07-12.
 */
public class LazyFragment extends BaseFragment {

    public static final String KEY_IS_LAZY_LOAD = "isLazyLoad";

    private boolean isLazyLoad = true;

    private boolean isInit = false;

    private Bundle savedInstanceState;

    protected boolean isLazyLoad() {
        return isLazyLoad;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_LAZY_LOAD, isLazyLoad);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;

        if (args != null) {
            isLazyLoad = args.getBoolean(KEY_IS_LAZY_LOAD, true);
        }
    }

    @Override
    public BaseView onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaseView baseView = super.onCreateView(inflater, container, savedInstanceState);

        if (isLazyLoad) {
            if (getUserVisibleHint() && !isInit) {
                isInit = true;
                this.savedInstanceState = savedInstanceState;
                onCreateViewLazy(baseView, savedInstanceState);
            }
        } else {
            isInit = true;
            onCreateViewLazy(baseView, savedInstanceState);
        }

        return baseView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !isInit && getContentView() != null) {
            isInit = true;
            onCreateViewLazy(getContentView(), savedInstanceState);
            onResumeLazy();
        }
        if (isInit && getContentView() != null) {
            if (isVisibleToUser) {
                isStart = true;
                onFragmentStartLazy();
            } else {
                isStart = false;
                onFragmentStopLazy();
            }
        }
    }

    @Deprecated
    @Override
    public final void onStart() {
        super.onStart();
        if (isInit && !isStart && getUserVisibleHint()) {
            isStart = true;
            onFragmentStartLazy();
        }
    }

    @Deprecated
    @Override
    public final void onStop() {
        super.onStop();
        if (isInit && isStart && getUserVisibleHint()) {
            isStart = false;
            onFragmentStopLazy();
        }
    }

    private boolean isStart = false;

    protected void onFragmentStartLazy() {

    }

    protected void onFragmentStopLazy() {

    }

    protected void onCreateViewLazy(BaseView baseView, @Nullable Bundle savedInstanceState) {

    }

    protected void onResumeLazy() {

    }

    protected void onPauseLazy() {

    }

    protected void onDestroyViewLazy() {

    }

    @Override
    @Deprecated
    public final void onResume() {
        super.onResume();
        if (isInit) {
            onResumeLazy();
        }
    }

    @Override
    @Deprecated
    public final void onPause() {
        super.onPause();
        if (isInit) {
            onPauseLazy();
        }
    }

    @Override
    @Deprecated
    public void onDestroyView() {
        super.onDestroyView();
        if (isInit) {
            onDestroyViewLazy();
        }
        isInit = false;
    }
}
