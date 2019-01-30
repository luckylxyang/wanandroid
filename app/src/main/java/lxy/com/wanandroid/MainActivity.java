package lxy.com.wanandroid;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.lang.reflect.Field;

import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.home.view.HomeFragment;
import lxy.com.wanandroid.officeaccount.OfficeAccountFragment;
import lxy.com.wanandroid.project.ProjectFragment;


/**
 * @author lxy
 */
public class MainActivity extends BaseActivity {

    private BottomNavigationView tabLayout;
    private HomeFragment homeFragment;
    private KnowledgeFragment knowledgeFragment;
    private OfficeAccountFragment officeAccountFragment;
    private ProjectFragment projectFragment;

    @Override
    protected void initOptions() {
        initView();
        initListener();
        addFragment(homeFragment,"HomeFragment");
    }

    private void initView() {
        tabLayout = findViewById(R.id.home_activity_navigate);
        disableShiftMode(tabLayout);
        homeFragment = new HomeFragment();
        knowledgeFragment = new KnowledgeFragment();
        officeAccountFragment = new OfficeAccountFragment();
        projectFragment = new ProjectFragment();
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
                    case R.id.navigation_dashboard:
                        hiddenAllFragment();
                        addFragment(knowledgeFragment,"KnowledgeFragment");
                        break;
                    case R.id.navigation_notifications:
                        hiddenAllFragment();
                        addFragment(projectFragment,"ProjectFragment");
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

    /**
     * Java反射得到BottomNavigationMenuView，关闭item >= 4 时的默认效果
     * @param navigationView
     */
    @SuppressLint("RestrictedApi")
    public void disableShiftMode(BottomNavigationView navigationView) {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigationView.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);

            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
                itemView.setShiftingMode(false);
                itemView.setChecked(itemView.getItemData().isChecked());
            }

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
