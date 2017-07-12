package com.klzb.live.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/3.
 * Author: XuDeLong
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Toast mToast;
    private long mLastBackClickTime = 0L;
    private long mExitInterval = 2000L;
    private boolean doubleBack = false;
    protected boolean isActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.bind(this);
        isActive = true;
    }

    public abstract int getLayoutResource();

    public void toast(String s) {
        if (isActive) {
            if (mToast == null) {
                mToast = Toast.makeText(this, s, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(s);
            }
            mToast.show();
        }
    }

    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }

    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void openActivityForResult(Class<?> cls, int requestCode) {
        openActivityForResult(cls, null, requestCode);
    }

    public void openActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void translateBottomIn(View view) {
        view.clearAnimation();
        view.setAnimation(AnimationUtils.loadAnimation(this, com.smart.androidutils.R.anim.bottom_in));
        view.setVisibility(View.VISIBLE);
    }

    public void translateBottomOut(View view) {
        view.clearAnimation();
        view.setAnimation(AnimationUtils.loadAnimation(this, com.smart.androidutils.R.anim.bottom_out));
        view.setVisibility(View.GONE);
    }

    public void setDoubleBack(boolean doubleBack) {
        this.doubleBack = doubleBack;
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            if ((System.currentTimeMillis() - mLastBackClickTime) > mExitInterval) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mLastBackClickTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActive = false;
    }
}