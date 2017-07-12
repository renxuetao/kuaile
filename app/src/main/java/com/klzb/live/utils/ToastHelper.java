package com.klzb.live.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by fengjh on 16/9/14.
 */
public class ToastHelper {

    public static final int LENGTH_LONG = 3500;
    public static final int LENGTH_SHORT = 2000;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowParams;
    private View toastView;
    private Context mContext;
    private Handler mHandler;
    private String mToastContent = "";
    private int duration = 0;
    private int animStyleId = android.R.style.Animation_Toast;

    private final Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            removeView();
        }
    };

    private ToastHelper(Context context){
        Context ctx = context.getApplicationContext();
        if (ctx == null){
            ctx = context;
        }
        this.mContext = ctx;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        init();
    }

    private void init(){
        mWindowParams = new WindowManager.LayoutParams();
        mWindowParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        mWindowParams.alpha = 1.0f;
        mWindowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM;
        mWindowParams.format = PixelFormat.TRANSLUCENT;
        mWindowParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        mWindowParams.setTitle("ToastHelper");
        mWindowParams.y = mContext.getResources().getDisplayMetrics().widthPixels / 5;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    private View getDefaultToastView(){
        TextView view = new TextView(mContext);
        view.setText(mToastContent);
        view.setGravity(Gravity.CENTER_VERTICAL|Gravity.START);
        view.setFocusable(false);
        view.setClickable(false);
        view.setFocusableInTouchMode(false);
        view.setPadding(15,10,15,10);
        Drawable drawable = mContext.getResources().getDrawable(android.R.drawable.toast_frame);
        if (Build.VERSION.SDK_INT<16){
            view.setBackgroundDrawable(drawable);
        }else{
            view.setBackground(drawable);
        }
        return view;
    }

    public void show(){
        removeView();
        if (toastView == null){
            toastView = getDefaultToastView();
        }
        mWindowParams.gravity = GravityCompat.getAbsoluteGravity(Gravity.CENTER_HORIZONTAL|Gravity.BOTTOM, ViewCompat.getLayoutDirection(toastView));
        removeView();
        mWindowManager.addView(toastView,mWindowParams);
        if (mHandler == null){
            mHandler = new Handler();
        }
        mHandler.postDelayed(timeRunnable,duration);
    }

    private void removeView(){
        if (toastView !=null && toastView.getParent()!=null){
            mWindowManager.removeView(toastView);
            mHandler.removeCallbacks(timeRunnable);
        }
    }

    public static ToastHelper makeText(Context context,String content,int duration){
        ToastHelper helper = new ToastHelper(context);
        helper.setDuration(duration);
        helper.setContent(content);
        return helper;
    }

    public static ToastHelper makeText(Context context,int strId,int duration){
        ToastHelper helper = new ToastHelper(context);
        helper.setDuration(duration);
        helper.setContent(context.getString(strId));
        return helper;
    }

    public ToastHelper setContent(String content){
        this.mToastContent = content;
        return this;
    }

    public ToastHelper setDuration(int duration){
        this.duration = duration;
        return this;
    }

    public ToastHelper setAnimation(int animStyleId){
        this.animStyleId = animStyleId;
        mWindowParams.windowAnimations = this.animStyleId;
        return this;
    }

    public ToastHelper setView(View view){
        this.toastView = view;
        return this;
    }
}
