package com.fpliu.newton.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

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

    //是否需要使用懒加载的标志
    private boolean needLazyLoad = true;

    //是否被创建了
    private boolean isCreated = false;

    //是否被初始化了
    private boolean isInitialized = false;

    //是否启动了
    private boolean isStarted = false;

    private Bundle savedInstanceState;

    protected boolean isLazyLoad() {
        return needLazyLoad;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_IS_LAZY_LOAD, needLazyLoad);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = savedInstanceState == null ? getArguments() : savedInstanceState;

        if (args != null) {
            needLazyLoad = args.getBoolean(KEY_IS_LAZY_LOAD, true);
        }
        isCreated = true;
    }

    @Override
    public BaseView onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        BaseView baseView = super.onCreateView(inflater, container, savedInstanceState);

        if (needLazyLoad) {
            if (getUserVisibleHint() && !isInitialized) {
                isInitialized = true;
                this.savedInstanceState = savedInstanceState;
                onCreateViewLazy(baseView, savedInstanceState);
            }
        } else {
            isInitialized = true;
            onCreateViewLazy(baseView, savedInstanceState);
        }

        return baseView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && !isInitialized && isCreated) {
            isInitialized = true;
            onCreateViewLazy(getContentView(), savedInstanceState);
            onResumeLazy();
        }
        if (isInitialized && isCreated) {
            if (isVisibleToUser) {
                isStarted = true;
                onFragmentStartLazy();
            } else {
                isStarted = false;
                onFragmentStopLazy();
            }
        }
    }

    @Deprecated
    @Override
    public final void onStart() {
        super.onStart();
        if (isInitialized && !isStarted && getUserVisibleHint()) {
            isStarted = true;
            onFragmentStartLazy();
        }
    }

    @Deprecated
    @Override
    public final void onStop() {
        super.onStop();
        if (isInitialized && isStarted && getUserVisibleHint()) {
            isStarted = false;
            onFragmentStopLazy();
        }
    }

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
        if (isInitialized) {
            onResumeLazy();
        }
    }

    @Override
    @Deprecated
    public final void onPause() {
        super.onPause();
        if (isInitialized) {
            onPauseLazy();
        }
    }

    @Override
    @Deprecated
    public void onDestroyView() {
        super.onDestroyView();
        if (isInitialized) {
            onDestroyViewLazy();
        }
        isInitialized = false;
        isCreated = false;
    }
}
