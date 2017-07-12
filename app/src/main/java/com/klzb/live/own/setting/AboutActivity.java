package com.klzb.live.own.setting;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.OnClick;
import com.klzb.live.R;
import com.klzb.live.base.BaseActivity;

public class AboutActivity extends BaseActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        AboutActivity.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("关于");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_about;
    }
}
