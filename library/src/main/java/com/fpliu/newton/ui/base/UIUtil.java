package com.fpliu.newton.ui.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 与UI相关的帮助类
 *
 * @author 792793182@qq.com 2015-06-12
 */
public final class UIUtil {

    private static final String TAG = UIUtil.class.getSimpleName();

    private UIUtil() {
    }

    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, double dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dip
     */
    public static int px2dip(Context context, double pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5);
    }

    /**
     * 根据手机分辨率从sp 的单位转化为px(像素)
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

    /**
     * 获取屏幕宽度
     *
     * @return 屏幕宽度（单位：px）
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return 屏幕高度（单位：px）
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }

    /**
     * 获取屏幕密度
     *
     * @return
     */
    public static float getScreenDensity(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(metric);
        return metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
    }

    /**
     * 获取状态栏的高度
     *
     * @param window 窗口
     * @return 状态栏高度（单位：px）
     */
    public static int getStatusBarHeight(Window window) {
        int statusBarHeight = 0;
        if (null != window) {
            //获取状态栏的高度
            Rect frame = new Rect();
            window.getDecorView().getWindowVisibleDisplayFrame(frame);
            statusBarHeight = frame.top;
        }
        if (0 == statusBarHeight) {
            statusBarHeight = 50;
        }
        return statusBarHeight;
    }

    /**
     * 显示或者隐藏状态栏
     *
     * @param window
     * @param show   是否显示
     */
    public static void showOrHideStatusBar(Window window, boolean show) {
        if (window == null) {
            return;
        }

        WindowManager.LayoutParams lp = window.getAttributes();
        if (show) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            window.setAttributes(lp);
            window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            window.setAttributes(lp);
            window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * 开启Window级别的硬件加速
     *
     * @param window
     */
    public static void openHardwareAccelarate(Window window) {
        //Android从3.0开始提供此接口
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                @SuppressWarnings("rawtypes")
                Class clazz = Class.forName("android.view.WindowManager$LayoutParams");
                Field flagField = clazz.getDeclaredField("FLAG_HARDWARE_ACCELERATED");
                Field maskField = clazz.getDeclaredField("FLAG_HARDWARE_ACCELERATED");
                int flags = flagField.getInt(null);
                int mask = maskField.getInt(null);
                Method method = Window.class.getDeclaredMethod("setFlags", int.class, int.class);
                method.invoke(window, flags, mask);
            } catch (Exception e) {
                Log.e(TAG, "openHardwareAccelarate()", e);
            }
        }
    }

    /**
     * 关闭View级别的硬件加速
     *
     * @param view
     */
    public static void closeLayerHardware(View view) {
        if (Build.VERSION.RELEASE.startsWith("4.")) {
            try {
                @SuppressWarnings("rawtypes")
                Class[] argClass = new Class[2];
                argClass[0] = int.class;
                argClass[1] = Paint.class;
                Method setLayerType = View.class.getDeclaredMethod(
                        "setLayerType", argClass);
                setLayerType.setAccessible(true);

                Field LAYER_TYPE_SOFTWARE = View.class
                        .getDeclaredField("LAYER_TYPE_SOFTWARE");
                LAYER_TYPE_SOFTWARE.setAccessible(true);
                int value = LAYER_TYPE_SOFTWARE.getInt(null);

                Object[] argValue = new Object[2];
                argValue[0] = value;
                argValue[1] = null;
                setLayerType.invoke(view, argValue);
            } catch (Exception e) {
                Log.e(TAG, "closeLayerHardware()", e);
            }
        }
    }

    /**
     * 弹出软键盘
     *
     * @param context 上下文
     * @param view    在哪个视图上显示
     */
    public static void showSoftInput(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 弹出软键盘
     *
     * @param context   上下文
     * @param view      在哪个视图上显示
     * @param delayTime 延迟时间（单位：ms）
     */
    public static void showSoftInputDelay(final Context context, final View view, int delayTime) {
        if (context == null || view == null || delayTime < 0) {
            return;
        }

        if (delayTime == 0) {
            showSoftInput(context, view);
        } else {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {

                @Override
                public void run() {
                    showSoftInput(context, view);
                }
            }, delayTime);
        }
    }

    /**
     * 隐藏软键盘
     *
     * @param context 上下文
     * @param view    在哪个视图上显示
     */
    public static void hideSoftInput(Context context, View view) {
        if (context == null || view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive(view)) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 输入法是否已激活
     *
     * @param context
     * @param packageName 输入法应用的包名
     * @return
     */
    public static boolean isInputMethodEnabled(Context context, String packageName) {
        return getEnableInputMethodInfor(context, packageName) != null;
    }

    /**
     * 获取已激活输入法的详细信息
     *
     * @param context
     * @param packageName 输入法应用的包名
     * @return
     */
    public static InputMethodInfo getEnableInputMethodInfor(Context context, String packageName) {
        if (packageName == null) {
            return null;
        }
        final InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> imeInfoList = imm.getEnabledInputMethodList();

        if (imeInfoList != null) {
            for (InputMethodInfo imeInfo : imeInfoList) {
                if (packageName.equals(imeInfo.getPackageName())) {
                    return imeInfo;
                }
            }
        }
        return null;
    }

    /**
     * 输入法是否已启用
     *
     * @param context
     * @param packageName 输入法应用的包名
     * @return
     */
    public static boolean isInputMethodInUse(Context context, String packageName) {
        InputMethodInfo imeInfo = getEnableInputMethodInfor(context, packageName);
        if (imeInfo != null) {
            String ourId = imeInfo.getId();
            // 当前输入法id
            String curId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);

            if (ourId != null && ourId.equals(curId)) {
                return true;
            }
            return false;
        }
        return false;
    }

    private static class XY {
        //含有2个元素的一维数组，表示距离屏幕左上角的点，此处作为一个域变量是为了避免重复new
        private static int[] locationOfViewOnScreen = new int[2];
    }

    /**
     * 判断触摸点是否在给定的view上
     *
     * @param event 触摸事件
     * @param view  给定的view
     * @return
     */
    public static boolean isInMyView(MotionEvent event, View view) {
        //如果此时view被隐藏掉了，触摸点肯定不会落在此view上
        if (view.getVisibility() == View.GONE) {
            return false;
        }

        //获取此view在屏幕上的位置（以屏幕左上角为参照点）
        view.getLocationOnScreen(XY.locationOfViewOnScreen);

        //获取触摸点相对于屏幕左上角的偏移量
        float rawX = event.getRawX();
        float rawY = event.getRawY();

        //如果触摸点处于此view的矩形区域内
        return rawX >= XY.locationOfViewOnScreen[0]
                && rawX <= (XY.locationOfViewOnScreen[0] + view.getWidth())
                && rawY >= XY.locationOfViewOnScreen[1]
                && rawY <= (XY.locationOfViewOnScreen[1] + view.getHeight());
    }

    /**
     * 设置在系统中改变字体大小不会影响我们的自体大小，如要考虑到美观
     * 此方法只在第一个Activity中调用就可以了，不需要在所有的Activity中调用
     *
     * @param activity
     */
    public static void remainFont(Activity activity) {
        Resources resources = activity.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.fontScale = 1.0f;
        resources.updateConfiguration(configuration, null);
    }

    /**
     * 设置屏幕方向
     *
     * @param activity 要设置屏幕方向的Activity
     * @param portrait 是否是竖屏
     */
    public static void changeScreenOrientation(Activity activity, boolean portrait) {
        if (activity == null) {
            return;
        }

        if (portrait) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        } else {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    /**
     * 截图 截图会引用一个bitmap对象，有时候会很大，需要释放调用view.destroyDrawingCache();
     *
     * @param view 要截图的对象
     * @return 截图
     */
    public static Bitmap snapshot(View view) {
        if (view == null) {
            return null;
        }

        //先销毁旧的
        view.destroyDrawingCache();

        //设置为可以截图
        view.setDrawingCacheEnabled(true);

        //获取截图
        return view.getDrawingCache();
    }

    public static int getTotalHeightofListView(ListView listView) {
        ListAdapter adapter = listView.getAdapter();
        if (adapter == null) {
            return 0;
        }
        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View convertView = adapter.getView(i, null, listView);
            convertView.measure(0, 0);
            totalHeight += convertView.getMeasuredHeight();
        }
        totalHeight += listView.getDividerHeight() * (adapter.getCount() - 1);
        return totalHeight;
    }

    public static int getTotalHeightOfGridView(GridView gridView, int numColumns) {
        ListAdapter adapter = gridView.getAdapter();
        if (adapter == null) {
            return 0;
        }
        int totalHeight = 0;
        int count = adapter.getCount();
        for (int i = 0; i < count; i += numColumns) {
            View convertView = adapter.getView(i, null, gridView);
            convertView.measure(0, 0);
            totalHeight += convertView.getMeasuredHeight();
        }
        return totalHeight;
    }

    /**
     * 获得焦点
     *
     * @param view
     */
    public static void obtainFocus(View view) {
        if (view != null) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
            view.requestFocus();

            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                String text = editText.getText().toString();
                int length = text.length();
                editText.setSelection(length, length);
            }
        }
    }

    public static void lostFocus(View view) {
        if (view != null) {
            view.setFocusable(false);
            view.setFocusableInTouchMode(false);
        }
    }

    /**
     * @param textView
     */
    public static void underline(TextView textView) {
        if (textView != null) {
            textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }
    }

    public static boolean isAppOnForeground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // Returns a list of application processes that are running on the device
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // importance:
            // The relative importance level that the system places
            // on this process.
            // May be one of IMPORTANCE_FOREGROUND, IMPORTANCE_VISIBLE,
            // IMPORTANCE_SERVICE, IMPORTANCE_BACKGROUND, or IMPORTANCE_EMPTY.
            // These constants are numbered so that "more important" values are
            // always smaller than "less important" values.
            // processName:
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(context.getPackageName())
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static boolean isActivityOnForeground(Context context, Class<? extends Activity> activityClazz) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName topActivity = runningTaskInfos.get(0).topActivity;
            String activityPackageName = topActivity.getPackageName();
            String currentPackageName = context.getPackageName();
            return activityPackageName.equals(currentPackageName) && topActivity.getClassName().equals(activityClazz.getName());
        }
        return false;
    }

    /**
     * 打开网络设置
     *
     * @param context 上下文
     */
    public static boolean startNetSettingActivity(Context context) {
        if (context == null) {
            return false;
        }

        Intent intent = null;
        if (Build.VERSION.SDK_INT < 14) {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        } else {
            intent = new Intent(Settings.ACTION_SETTINGS);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            Log.e(TAG, "startNetSetting()", e);
            return false;
        }
    }

    public static ColorStateList newColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        return new ColorStateList(states, colors);
    }

    public static boolean isNetworkAvailable(Context context) {
        // 获取系统的连接服务
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnected();
    }

    public static Snackbar showSnackBar(View view, String message, int duration) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        //防止崩溃掉
        try {
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
        } catch (Exception e) {
            // do nothing
        }

        snackbar.setActionTextColor(BaseUIConfig.getTitleColor());
        snackbar.setAction("确定", v -> snackbar.dismiss());
        snackbar.show();
        return snackbar;
    }

    public final Snackbar showSnackBarWithAction(View view, String message, int duration, String actionText, View.OnClickListener onClickListener) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        //防止崩溃掉
        try {
            ((TextView) snackbar.getView().findViewById(R.id.snackbar_text)).setTextColor(Color.WHITE);
        } catch (Exception e) {
            // do nothing
        }

        snackbar.setAction(actionText, onClickListener);
        snackbar.show();
        return snackbar;
    }

    /**
     * @param context  上下文
     * @param text     要显示的文本
     * @param duration 显示时长，单位：毫秒
     */
    public static Toast makeToast(Context context, CharSequence text, int duration) {
        Toast toast = new Toast(context);
        toast.setDuration(duration);
        toast.setGravity(Gravity.CENTER, 0, 0);

        TextView textView = new TextView(context);
        int width = dip2px(context, 20);
        int height = dip2px(context, 20);
        textView.setPadding(width, height, width, height);
        textView.setBackgroundDrawable(getRoundRectShapeDrawable(Color.parseColor("#BA000000")));
        textView.setText(text);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(16);
        toast.setView(textView);

        return toast;
    }

    public static Toast makeToast(Context context, int stringId, int duration) {
        return makeToast(context, context.getResources().getText(stringId), duration);
    }

    public static ShapeDrawable getRoundRectShapeDrawable(int color) {
        float r = 10;
        float[] outerR = new float[]{r, r, r, r, r, r, r, r};
        RoundRectShape rr = new RoundRectShape(outerR, null, null);
        ShapeDrawable drawable = new ShapeDrawable(rr);
        drawable.getPaint().setColor(color);
        return drawable;
    }
}
