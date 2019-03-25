package lxy.com.wanandroid.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import lxy.com.wanandroid.R;

/**
 * @author lxy
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected FrameLayout flContext;
    protected Toolbar toolbar;
    protected Bundle saveBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        saveBundle = savedInstanceState;

        init();
        addContextView();
        initOptions();
    }


    /**
     * 设置 activity 内容
     * @return
     */
    public abstract int setContextView();

    protected abstract void initOptions();

    private void init() {
        flContext = findViewById(R.id.base_activity_context);

        toolbar = findViewById(R.id.base_activity_toolbar);
        setSupportActionBar(toolbar);
    }


    public void addContextView(){
        View view = LayoutInflater.from(this).inflate(setContextView(),null);
        flContext.addView(view);
    }

    protected void showToolbarBack(boolean isShow){
        if (isShow){
            Drawable upArrow = ContextCompat.getDrawable(this, R.drawable.abc_ic_ab_back_material);
            upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(v -> finish());
        }
    }

    protected void setToolbarTitle(String title){
        showToolbarBack(true);
        getSupportActionBar().setTitle(title);
    }

}
