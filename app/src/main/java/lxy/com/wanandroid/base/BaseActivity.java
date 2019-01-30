package lxy.com.wanandroid.base;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import lxy.com.wanandroid.R;

/**
 * @author lxy
 */
public abstract class BaseActivity extends AppCompatActivity {

    private FrameLayout flContext;
    protected Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        initView();
        addContextView();
        initOptions();
    }


    /**
     * 设置 activity 内容
     * @return
     */
    public abstract int setContextView();

    protected abstract void initOptions();

    private void initView() {
        flContext = findViewById(R.id.base_activity_context);

        toolbar = findViewById(R.id.base_activity_toolbar);
        setSupportActionBar(toolbar);
    }


    public void addContextView(){
        flContext.addView(View.inflate(this,setContextView(),null));
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
        getSupportActionBar().setTitle(title);
    }

}
