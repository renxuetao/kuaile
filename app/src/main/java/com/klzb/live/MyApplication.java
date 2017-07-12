package com.klzb.live;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVOSCloud;
import com.baidu.location.LocationClient;
import com.tencent.bugly.crashreport.CrashReport;

import cn.leancloud.chatkit.LCChatKit;

import com.klzb.live.lean.CustomUserProvider;

/**
 * Created by Administrator on 2016/9/1.
 * Author: XuDeLong
 */
public class MyApplication extends Application {

    public LocationService locationService;
    public LocationClient locationClient;
    private final String APP_ID = "jtT3mAOmAUWe2aqzFJurXpjH-gzGzoHsz";
    private final String APP_KEY = "SslHVVjeDR84DlcWaaMGktcK";
    String balance = "0";
    private static Context mContext;

    public static Context getGlobalContext() {
        return mContext;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        MultiDex.install(this);
        LCChatKit.getInstance().init(getApplicationContext(), APP_ID, APP_KEY);
        LCChatKit.getInstance().setProfileProvider(CustomUserProvider.getInstance());
        //locationService = new LocationService(getApplicationContext());
        locationClient = new LocationClient(this);
        CrashReport.initCrashReport(getApplicationContext(), "0010b50033", true);//bugly 腾讯软件分析
    }
}
