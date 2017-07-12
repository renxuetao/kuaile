package com.klzb.live.utils;

import android.content.Context;
import android.widget.TextView;

import com.klzb.live.R;

/**
 * Created by Administrator on 2017/3/2.
 * Author: XuDeLong
 */

public class FunctionUtile {

    public static void setLevel(Context context, TextView v, int level ){
        if(level < 1){
            v.setBackground(context.getResources().getDrawable(R.drawable.level1));
        }else if(level > 0 && level < 4){
            v.setBackground(context.getResources().getDrawable(R.drawable.level1));
        }else if(level > 3 && level < 6){
            v.setBackground(context.getResources().getDrawable(R.drawable.level2));
        }else if(level > 5 && level < 11){
            v.setBackground(context.getResources().getDrawable(R.drawable.level3));
        }else if(level > 10 && level < 16){
            v.setBackground(context.getResources().getDrawable(R.drawable.level4));
        }else if(level > 15 && level < 21){
            v.setBackground(context.getResources().getDrawable(R.drawable.level5));
        }else if(level > 20 && level < 31){
            v.setBackground(context.getResources().getDrawable(R.drawable.level6));
        }else if(level > 30 && level < 41){
            v.setBackground(context.getResources().getDrawable(R.drawable.level7));
        }else if(level > 50){
            v.setBackground(context.getResources().getDrawable(R.drawable.level8));
        }
    }
}
