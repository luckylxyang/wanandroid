package lxy.com.wanandroid;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


/**
 * @author lxy
 */
public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        toolbar = findViewById(R.id.home_activity_toolbar);
        tabLayout = findViewById(R.id.home_activity_tabLayout);

        setSupportActionBar(toolbar);
    }
}
