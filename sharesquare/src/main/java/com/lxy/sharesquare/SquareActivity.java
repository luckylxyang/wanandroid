package com.lxy.sharesquare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.lxy.basemodel.base.BaseActivity;
import com.lxy.basemodel.base.Constants;

@Route(path = Constants.URL_SQUARE_ACTIVITY)
public class SquareActivity extends BaseActivity {

    @Override
    public int setContextView() {
        return R.layout.activity_square;
    }

    @Override
    protected void initOptions() {

    }
}
