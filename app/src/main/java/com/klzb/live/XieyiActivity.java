package com.klzb.live;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.klzb.live.base.BaseEditActivity;

import butterknife.OnClick;

public class XieyiActivity extends BaseEditActivity {

    @OnClick(R.id.image_back)
    public void back() {
        finish();
    }

    public static void newInstance(Context context) {
        ((Activity) context).startActivity(new Intent(context, XieyiActivity.class));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_xieyi;
    }
}
