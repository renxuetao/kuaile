package com.klzb.live;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.smart.androidui.widget.menu.BottomTabMenu;
import com.smart.androidui.widget.menu.OnTabClickListener;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import cn.leancloud.chatkit.LCChatKit;
import com.klzb.live.base.BaseActivity;
import com.klzb.live.intf.OnRequestDataListener;
import com.klzb.live.living.PublishActivity;
import com.klzb.live.own.PushActivity;
import com.klzb.live.utils.Api;

public class MainActivity extends BaseActivity {

    @Bind(R.id.bottomTabMenu)
    BottomTabMenu mBottomTabMenu;
    private Fragment[] mTabFragment = new Fragment[2];
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @OnClick(R.id.image_start_living)
    public void startLiving(View view) {
        openActivity(PublishActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        if ("".equals(SharePrefsUtils.get(this, "user", "token", "")) || "".equals(SharePrefsUtils.get(this, "user", "userId", ""))) {
//           // openActivity(LoginActivity.class);
//            Intent intent = new Intent(this,LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//            finish();
//            return;
//        }
        PushService.setDefaultPushCallback(this, PushActivity.class);

        setDoubleBack(true);
        mFragmentManager = getSupportFragmentManager();
        mTabFragment[0] = mFragmentManager.findFragmentById(R.id.fragment_home);
        mTabFragment[1] = mFragmentManager.findFragmentById(R.id.fragment_own);
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.hide(mTabFragment[0]).hide(mTabFragment[1]);
        mTransaction.show(mTabFragment[0]).commit();
        mBottomTabMenu.setHasMiddleImage(true);
        mBottomTabMenu.addImageTab(R.drawable.zhibo_shou, R.drawable.zhibo_shou_s);
        mBottomTabMenu.addImageTab(R.drawable.zhibo_me, R.drawable.zhibo_me_s);
        mBottomTabMenu.changeImageTab(0);
        mBottomTabMenu.setOnTabClickListener(new OnTabClickListener() {
            @Override
            public void onTabClick(View view, int position) {
                mTransaction = mFragmentManager.beginTransaction();
                mTransaction.hide(mTabFragment[0]).hide(mTabFragment[1]);
                mTransaction.show(mTabFragment[position]).commit();
            }
        });
        LCChatKit.getInstance().open((String)SharePrefsUtils.get(this, "user", "userId", ""), new AVIMClientCallback() {
            @Override
            public void done(AVIMClient avimClient, AVIMException e) {

            }
        });
        JSONObject params = new JSONObject();
        try {
            String versionCode = getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            params.put("ver_num",versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        Api.checkUpdate(this,params , new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                JSONObject info = data.getJSONObject("data");
                if(StringUtils.isNotEmpty(info.getString("package"))){
                    checkUpgrade(info.getString("package"),info.getString("description"));
                }
            }

            @Override
            public void requestFailure(int code, String msg) {
                //toast(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    AlertDialog tipsAlertDialog;
    private void checkUpgrade(final String downloadUrl,String mes) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        tipsAlertDialog = builder.setTitle("提示")
                .setMessage(mes)
                .setNegativeButton("再等一下", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (tipsAlertDialog.isShowing()) {
                            tipsAlertDialog.dismiss();
                        }
                    }
                })
                .setPositiveButton("更新下载", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri uri = Uri.parse(downloadUrl);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .create();
        tipsAlertDialog.show();
        tipsAlertDialog.setCancelable(false);
        tipsAlertDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_main;
    }

}
