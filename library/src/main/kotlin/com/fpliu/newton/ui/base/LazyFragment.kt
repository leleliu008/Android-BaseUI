package com.fpliu.newton.ui.base

import android.os.Bundle
import android.view.View
import com.fpliu.newton.log.Logger

/**
 * <h1>懒加载Fragment</h1> 只有创建并显示的时候才会调用onCreateViewLazy方法<br></br>
 * <br></br>
 * <br></br>
 * 懒加载的原理onCreateView的时候Fragment有可能没有显示出来。<br></br>
 * 但是调用到setUserVisibleHint(boolean isVisibleToUser),isVisibleToUser =
 * true的时候就说明有显示出来<br></br>
 * 但是要考虑onCreateView和setUserVisibleHint的先后问题所以才有了下面的代码
 * <br></br>
 * 注意：<br></br>
 * 《1》原先的Fragment的回调方法名字后面要加个Lazy，比如Fragment的onCreateView方法， 就写成onCreateViewLazy <br></br>
 * 《2》使用该LazyFragment会导致多一层布局深度
 *
 * @author 792793182@qq.com 2016-07-12.
 */
abstract class LazyFragment : BaseFragment() {

    companion object {
        private val TAG = LazyFragment::class.java.simpleName
        private const val KEY_IS_LAZY_LOAD = "isLazyLoad"
    }

    //是否需要使用懒加载的标志
    protected var isLazyLoad = true
        private set

    //是否被创建了
    private var isCreated = false

    //是否被初始化了
    private var isInitialized = false

    //是否启动了
    private var isStarted = false

    private var savedInstanceState: Bundle? = null

    override fun onSaveInstanceState(outState: Bundle) {
        Logger.i(TAG, "onSaveInstanceState() outState = $outState")
        super.onSaveInstanceState(outState)
        outState.putBoolean(KEY_IS_LAZY_LOAD, isLazyLoad)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Logger.i(TAG, "onCreate() savedInstanceState = $savedInstanceState")
        super.onCreate(savedInstanceState)
        isLazyLoad = (savedInstanceState ?: arguments)?.getBoolean(KEY_IS_LAZY_LOAD, true) ?: true
        isCreated = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Logger.i(TAG, "onViewCreated() savedInstanceState = $savedInstanceState")
        super.onViewCreated(view, savedInstanceState)
        if (isLazyLoad) {
            if (userVisibleHint && !isInitialized) {
                isInitialized = true
                this.savedInstanceState = savedInstanceState
                onCreateViewLazy(view as BaseView, savedInstanceState)
            }
        } else {
            isInitialized = true
            onCreateViewLazy(view as BaseView, savedInstanceState)
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        Logger.i(TAG, "setUserVisibleHint() isVisibleToUser = $isVisibleToUser")
        super.setUserVisibleHint(isVisibleToUser)

        if (isVisibleToUser && !isInitialized && isCreated) {
            isInitialized = true
            onCreateViewLazy(contentView, savedInstanceState)
            onResumeLazy()
        }
        if (isInitialized && isCreated) {
            if (isVisibleToUser) {
                isStarted = true
                onFragmentStartLazy()
            } else {
                isStarted = false
                onFragmentStopLazy()
            }
        }
    }

    override fun onStart() {
        Logger.i(TAG, "onStart()")
        super.onStart()
        if (isInitialized && !isStarted && userVisibleHint) {
            isStarted = true
            onFragmentStartLazy()
        }
    }

    override fun onResume() {
        Logger.i(TAG, "onResume()")
        super.onResume()
        if (isInitialized) {
            onResumeLazy()
        }
    }

    override fun onPause() {
        Logger.i(TAG, "onPause()")
        super.onPause()
        if (isInitialized) {
            onPauseLazy()
        }
    }

    override fun onStop() {
        Logger.i(TAG, "onStop()")
        super.onStop()
        if (isInitialized && isStarted && userVisibleHint) {
            isStarted = false
            onFragmentStopLazy()
        }
    }

    override fun onDestroyView() {
        Logger.i(TAG, "onDestroyView()")
        super.onDestroyView()
        if (isInitialized) {
            onDestroyViewLazy()
        }
        isInitialized = false
        isCreated = false
    }

    protected open fun onFragmentStartLazy() {
        Logger.i(TAG, "onFragmentStartLazy()")
    }

    protected open fun onFragmentStopLazy() {
        Logger.i(TAG, "onFragmentStopLazy()")
    }

    protected open fun onCreateViewLazy(baseView: BaseView, savedInstanceState: Bundle?) {
        Logger.i(TAG, "onCreateViewLazy()")
    }

    protected open fun onResumeLazy() {
        Logger.i(TAG, "onResumeLazy()")
    }

    protected open fun onPauseLazy() {
        Logger.i(TAG, "onPauseLazy()")
    }

    protected open fun onDestroyViewLazy() {
        Logger.i(TAG, "onDestroyViewLazy()")
    }
}
