package com.klzb.live;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.smart.androidutils.utils.SharePrefsUtils;

import butterknife.Bind;
import com.klzb.live.base.BaseActivity;
import com.klzb.live.intf.OnRequestDataListener;
import com.klzb.live.login.LoginActivity;
import com.klzb.live.own.WebviewActivity;
import com.klzb.live.utils.Api;

public class SplashActivity extends BaseActivity {

    private boolean destroy = false;
    boolean firstOpen = false;
    @Bind(R.id.lauch_screen)
    ImageView mLauchScreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //isFirstOpen();
        if(!firstOpen){
//            Api.getLaunchScreen(this, new JSONObject(), new OnRequestDataListener() {
//                @Override
//                public void requestSuccess(int code, final JSONObject data) {
//                    if(isActive){
//                        Glide.with(getApplicationContext()).load(data.getString("info")).into(mLauchScreen);
//                        mLauchScreen.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                if(null != data.getString("url") && null != data.getString("title")){
//                                    Bundle temp = new Bundle();
//                                    temp.putString("title",data.getString("title"));
//                                    temp.putString("jump",data.getString("url"));
//                                    openActivity(WebviewActivity.class,temp);
//                                    SplashActivity.this.finish();
//                                }
//                            }
//                        });
//                    }
//
//                }
//
//                @Override
//                public void requestFailure(int code, String msg) {
//
//                }
//            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!destroy) {
                        if ("".equals(SharePrefsUtils.get(SplashActivity.this, "user", "token", "")) || "".equals(SharePrefsUtils.get(SplashActivity.this, "user", "userId", ""))) {
                            openActivity(LoginActivity.class);
                        }else {
                            openActivity(MainActivity.class);
                        }
                        SplashActivity.this.finish();
                    }
                }
            }, 3000);
        }


    }
    private void isFirstOpen(){
        firstOpen = (boolean) SharePrefsUtils.get(SplashActivity.this, "system", "isFirstOpen", true);
        if (firstOpen){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,FirstOpenActivity.class));
                    finish();
                    SharePrefsUtils.put(SplashActivity.this, "system", "isFirstOpen", false);
                }
            },2000);
        }
    }
    @Override
    public int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        destroy = true;
        super.onDestroy();
    }
}
