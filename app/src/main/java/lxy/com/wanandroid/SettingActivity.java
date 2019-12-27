package lxy.com.wanandroid;

import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;

import com.lxy.basemodel.base.BaseActivity;


public class SettingActivity extends BaseActivity {

    @Override
    public int setContextView() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initOptions() {
        setToolbarTitle(getString(R.string.set_set));
    }

    public void onChangModel(View view){
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if(mode == Configuration.UI_MODE_NIGHT_NO) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        recreate();
    }
}
