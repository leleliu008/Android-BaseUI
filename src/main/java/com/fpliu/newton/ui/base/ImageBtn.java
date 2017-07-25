package com.fpliu.newton.ui.base;

import android.content.Context;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

/**
 * 图片按钮
 *
 * @author 792793182@qq.com 2016-05-31.
 */
public class ImageBtn implements IHeadViewStrategy<ImageButton> {

    @Override
    public ImageButton onCreateView(RelativeLayout headView) {
        Context context = headView.getContext();
        ImageButton imageButton = new ImageButton(context);
        imageButton.setImageResource(R.drawable.ic_back_white);
        imageButton.setBackgroundResource(R.drawable.state_list_rectangle_transparent);
        int padding = UIUtil.dip2px(context, 20);
        imageButton.setPadding(padding, 0, padding, 0);
        return imageButton;
    }
}
