package com.fpliu.newton.ui.base;

import android.view.View;
import android.widget.RelativeLayout;

/**
 * 标题栏的策略
 *
 * @author 792793182@qq.com 2016-05-31.
 */
public interface IHeadViewStrategy<V extends View> {

    V getView(RelativeLayout parentView);
}
