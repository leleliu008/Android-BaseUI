package com.fpliu.newton.ui.base;

import android.graphics.Color;

/**
 * 全局配置
 *
 * @author 792793182@qq.com 2017-06-11.
 */
public final class GlobalConfig {

    private static int bgColor = Color.parseColor("#F2F2F2");

    private static int headBgColor = Color.parseColor("#00aff0");

    private static int headHeight = 60;

    private static int titlePaddingLeftRight = 60;

    private static String fontFileName;

    /**
     * 设置背景颜色
     *
     * @param bgColor 背景颜色
     */
    public static void setBgColor(int bgColor) {
        GlobalConfig.bgColor = bgColor;
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
        GlobalConfig.headBgColor = headBgColor;
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
        GlobalConfig.headHeight = headHeight;
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
        GlobalConfig.titlePaddingLeftRight = titlePaddingLeftRight;
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
        GlobalConfig.fontFileName = fontFileName;
    }

    public static String getFontFileName() {
        return GlobalConfig.fontFileName;
    }
}
