package com.klzb.live.own.userinfo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.smart.androidutils.utils.SharePrefsUtils;
import com.smart.androidutils.utils.StringUtils;

import butterknife.Bind;
import butterknife.OnClick;
import com.klzb.live.R;
import com.klzb.live.base.BaseEditActivity;
import com.klzb.live.intf.OnRequestDataListener;
import com.klzb.live.utils.Api;
import com.klzb.live.utils.TimeCountUtile;

public class MobileInActivity extends BaseEditActivity {

    @Bind(R.id.text_top_title)
    TextView mTextTopTitle;
    @Bind(R.id.edit_input_tel)
    EditText mEditInputTel;
    @Bind(R.id.edit_input_code)
    EditText mEditInputCode;

    @OnClick(R.id.image_top_back)
    public void back(View view) {
        MobileInActivity.this.finish();
    }

    @OnClick(R.id.btn_save)
    public void save(View view) {
        String Tel = mEditInputTel.getText().toString();
        String varcode = mEditInputCode.getText().toString();
        if (StringUtils.isEmpty(Tel) || StringUtils.isEmpty(varcode)) {
            toast("请将信息填写完整");
            return;
        }
        JSONObject params = new JSONObject();
        params.put("token", (String) SharePrefsUtils.get(this, "user", "token", ""));
        params.put("mobile_num", Tel);
        params.put("varcode", varcode);
        Api.changeMobile(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                toast(data.getString("descrp"));
                finish();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }

    @OnClick(R.id.btn_get_code)
    public void getCode(final View view) {
        final Button btn = (Button) view;
        String mobileNum = mEditInputTel.getText().toString();
        if (mobileNum.trim() == "") {
            toast("请输入正确的手机号");
            return;
        }

        JSONObject params = new JSONObject();
        params.put("mobile_num", mobileNum);
        params.put("status", "binding0");
        Api.getVarCode(this, params, new OnRequestDataListener() {
            @Override
            public void requestSuccess(int code, JSONObject data) {
                toast(data.getString("descrp"));
                TimeCountUtile timer = new TimeCountUtile(MobileInActivity.this, 60000, 1000, btn);
                timer.start();
            }

            @Override
            public void requestFailure(int code, String msg) {
                toast(msg);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTextTopTitle.setText("手机号绑定");
    }

    @Override
    public int getLayoutResource() {
        return R.layout.activity_mobile_in;
    }
}
