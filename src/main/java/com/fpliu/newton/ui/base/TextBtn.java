package com.fpliu.newton.ui.base;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * 文字按钮
 *
 * @author 792793182@qq.com 2016-05-31.
 */
public class TextBtn implements IHeadViewStrategy<Button> {

    @Override
    public Button getView(RelativeLayout headView) {
        Context context = headView.getContext();
        Button btn = new Button(context);
        btn.setText("提交");
        btn.setBackgroundColor(Color.TRANSPARENT);
        btn.setTextColor(UIUtil.newColorStateList(Color.GREEN, Color.GREEN, Color.GREEN, Color.GRAY));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        btn.setLayoutParams(new RelativeLayout.LayoutParams(UIUtil.dip2px(context, 60), RelativeLayout.LayoutParams.WRAP_CONTENT));
        return btn;
    }
}
