package com.fpliu.newton.ui.base

import android.content.Context
import android.view.View

/**
 * 标题栏的策略
 *
 * @author 792793182@qq.com 2016-05-31.
 */
interface IHeadViewStrategy<V : View> {

    fun onCreateView(context: Context): V
}
