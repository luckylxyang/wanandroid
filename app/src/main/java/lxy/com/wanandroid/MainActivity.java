package lxy.com.wanandroid;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.home.view.HomeFragment;
import lxy.com.wanandroid.officeaccount.OfficeAccountFragment;


/**
 * @author lxy
 */
public class MainActivity extends BaseActivity {

    private BottomNavigationView tabLayout;
    private HomeFragment homeFragment;
    private KnowledgeFragment knowledgeFragment;
    private OfficeAccountFragment officeAccountFragment;

    @Override
    protected void initOptions() {
        initView();
        initListener();
        addFragment(homeFragment,"HomeFragment");
    }

    private void initView() {
        tabLayout = findViewById(R.id.home_activity_navigate);

        homeFragment = new HomeFragment();
        knowledgeFragment = new KnowledgeFragment();
        officeAccountFragment = new OfficeAccountFragment();
    }

    @Override
    public int setContextView() {
        return R.layout.activity_main;
    }

    private void initListener(){
        tabLayout.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        hiddenAllFragment();
                        addFragment(homeFragment,"HomeFragment");
                        break;
                    case R.id.navigation_official_accounts:
                        hiddenAllFragment();
                        addFragment(officeAccountFragment,"OfficeAccountFragment");
                        break;
                    case R.id.navigation_notifications:
                        hiddenAllFragment();
                        addFragment(knowledgeFragment,"KnowledgeFragment");
                        break;
                    default:
                        break;

                }
                return true;
            }
        });
    }

    private void hiddenAllFragment(){
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            transition.hide(frag);
        }
        transition.commit();
    }

    private void addFragment(Fragment frag,String tag){
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        if (!getSupportFragmentManager().getFragments().contains(frag)){
            transition.add(R.id.home_activity_content,frag,tag);
        }else {
            transition.show(frag);
        }
        transition.commit();
    }
}
