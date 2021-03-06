package com.klzb.live.own;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.smart.androidutils.utils.SharePrefsUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import com.klzb.live.MainActivity;
import com.klzb.live.R;
import com.klzb.live.base.BaseActivity;
import com.klzb.live.login.LoginActivity;
import com.klzb.live.view.SFProgrssDialog;

public class WebviewActivity extends BaseActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.exchange_webview)
    WebView mExchangeWebView;
    private SFProgrssDialog dialog;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        if ("".equals(SharePrefsUtils.get(this, "user", "token", "")) || "".equals(SharePrefsUtils.get(this, "user", "userId", ""))) {
            openActivity(LoginActivity.class);
        }else {
            if(!isExsitMianActivity(MainActivity.class))
                openActivity(MainActivity.class);
        }
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle data = getIntent().getExtras();
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText(data.getString("title"));
        mExchangeWebView.loadUrl(data.getString("jump"));
        dialog = SFProgrssDialog.show(this, "请稍后...");
        WebSettings webSettings = mExchangeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mExchangeWebView.setWebViewClient(new myWebClient());
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_webview;
    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            dialog.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if ("".equals(SharePrefsUtils.get(this, "user", "token", "")) || "".equals(SharePrefsUtils.get(this, "user", "userId", ""))) {
            openActivity(LoginActivity.class);
        }else {
            if(!isExsitMianActivity(MainActivity.class))
                openActivity(MainActivity.class);
        }
        finish();
    }

    /**
     * 判断某一个类是否存在任务栈里面
     * @return
     */
    private boolean isExsitMianActivity(Class cls){
        Intent intent = new Intent(this, cls);
        ComponentName cmpName = intent.resolveActivity(getPackageManager());
        boolean flag = false;
        if (cmpName != null) { // 说明系统中存在这个activity
            ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(10);
            for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
                if (taskInfo.baseActivity.equals(cmpName)) { // 说明它已经启动了
                    flag = true;
                    break;  //跳出循环，优化效率
                }
            }
        }
        return flag;
    }


}
