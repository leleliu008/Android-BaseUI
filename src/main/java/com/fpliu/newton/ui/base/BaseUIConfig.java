package com.fpliu.newton.ui.base;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.Button;
import android.widget.RelativeLayout;

/**
 * 全局配置
 *
 * @author 792793182@qq.com 2017-06-11.
 */
public final class BaseUIConfig {

    private static int bgColor = Color.parseColor("#F2F2F2");

    private static int headBgColor = Color.parseColor("#00aff0");

    private static int headHeight = 60;

    private static int titlePaddingLeftRight = 60;

    private static String fontFileName;

    private static IHeadViewStrategy<?> leftBtn = new TextBtn() {

        @Override
        public Button onCreateView(RelativeLayout headView) {
            Context context = headView.getContext();
            Button btn = super.onCreateView(headView);
            btn.setText("返回");
            btn.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
            btn.setTextColor(Color.WHITE);
            btn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.btn_back_normal, 0, 0, 0);
            btn.setCompoundDrawablePadding(5);
            int padding = UIUtil.dip2px(context, 10);
            btn.setPadding(padding, 0, padding, 0);
            btn.setLayoutParams(new RelativeLayout.LayoutParams(UIUtil.dip2px(context, 90), RelativeLayout.LayoutParams.WRAP_CONTENT));
            return btn;
        }
    };

    /**
     * 设置背景颜色
     *
     * @param bgColor 背景颜色
     */
    public static void setBgColor(int bgColor) {
        BaseUIConfig.bgColor = bgColor;
    }

    public static int getBgColor() {
        return bgColor;
    }

    /**
     * 设置标题栏的背景颜色
     *
     * @param headBgColor 标题栏的背景颜色
     */
    public static void setHeadBgColor(int headBgColor) {
        BaseUIConfig.headBgColor = headBgColor;
    }

    public static int getHeadBgColor() {
        return headBgColor;
    }

    /**
     * 设置标题栏的高度）
     *
     * @param headHeight 标题栏的高度，单位：px
     */
    public static void setHeadHeight(int headHeight) {
        BaseUIConfig.headHeight = headHeight;
    }

    public static int getHeadHeight() {
        return headHeight;
    }

    /**
     * 设置标题栏两边的边距
     *
     * @param titlePaddingLeftRight 标题栏两边的边距，单位：px
     */
    public static void setTitlePaddingLeftRight(int titlePaddingLeftRight) {
        BaseUIConfig.titlePaddingLeftRight = titlePaddingLeftRight;
    }

    public static int getTitlePaddingLeftRight() {
        return titlePaddingLeftRight;
    }

    /**
     * 设置字体
     *
     * @param fontFileName 字体文件存放在assets目录中
     */
    public static void setFontFileName(String fontFileName) {
        BaseUIConfig.fontFileName = fontFileName;
    }

    public static String getFontFileName() {
        return BaseUIConfig.fontFileName;
    }

    public static void setDefaultLeftBtn(IHeadViewStrategy<?> defaultLeftBtn) {
        BaseUIConfig.leftBtn = defaultLeftBtn;
    }

    public static IHeadViewStrategy<?> getLeftBtn() {
        return leftBtn;
    }
}
