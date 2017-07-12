package com.klzb.live.base;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/3/3.
 * Author: XuDeLong
 */

public abstract class BaseFragment extends Fragment {

    private View mRootView;
    private Toast mToast;
    protected boolean isActive = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootView = inflater.inflate(getLayoutResource(), container, false);
        isActive = true;
        ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    public abstract int getLayoutResource();

    public void openActivity(Class<?> cls) {
        openActivity(cls, null);
    }

    public void openActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void toast(String s) {
        if (isActive) {
            if (mToast == null) {
                mToast = Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT);
            } else {
                mToast.setText(s);
            }
            mToast.show();
        }
    }

    public void openActivityForResult(Class<?> cls, int requestCode) {
        openActivityForResult(cls, null, requestCode);
    }

    public void openActivityForResult(Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    public void translateBottomIn(View view) {
        view.clearAnimation();
        view.setAnimation(AnimationUtils.loadAnimation(getActivity(), com.smart.androidutils.R.anim.bottom_in));
        view.setVisibility(View.VISIBLE);
    }

    public void translateBottomOut(View view) {
        view.clearAnimation();
        view.setAnimation(AnimationUtils.loadAnimation(getActivity(), com.smart.androidutils.R.anim.bottom_out));
        view.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isActive = false;
    }
}