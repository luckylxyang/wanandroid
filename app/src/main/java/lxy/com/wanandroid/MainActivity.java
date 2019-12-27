package lxy.com.wanandroid;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.lxy.basemodel.base.BaseActivity;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Field;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.base.FragmentInterface;
import lxy.com.wanandroid.collect.CollectActivity;
import lxy.com.wanandroid.home.HomeFragment;
import lxy.com.wanandroid.knowledge.KnowledgeFragment;
import lxy.com.wanandroid.network.BaseObserver;
import lxy.com.wanandroid.officeAccount.OfficeAccountActivity;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.project.ProjectFragment;
import lxy.com.wanandroid.search.HotActivity;
import lxy.com.wanandroid.search.SearchActivity;


/**
 * @author lxy
 */
public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView tabLayout;
    private HomeFragment homeFragment;
    private KnowledgeFragment knowledgeFragment;
    private ProjectFragment projectFragment;
    private DrawerLayout drawer;
    private View llNavHeader;
    private TextView tvUserName;
    private TextView tvHeader;
    private TextView tvUserEmail;
    private String TAGFrag = "HomeFragment";

    @Override
    protected void initOptions() {

        initView();
        initListener();
        if (saveBundle != null) {
            TAGFrag = saveBundle.getString("showFrag", "HomeFragment");
        }
        loadFragment();
    }

    private void loadFragment(){
        switch (TAGFrag) {
            case "HomeFragment":
                hiddenAllFragment();
                addFragment(homeFragment, "HomeFragment");
                break;
            case "KnowledgeFragment":
                hiddenAllFragment();
                addFragment(knowledgeFragment, "KnowledgeFragment");
                break;
            case "ProjectFragment":
                hiddenAllFragment();
                addFragment(projectFragment, "ProjectFragment");
                break;
        }
    }

    private void initView() {
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        llNavHeader = navigationView.getHeaderView(0);
        tvUserName = llNavHeader.findViewById(R.id.user_name);
        tvUserEmail = llNavHeader.findViewById(R.id.user_email);
        tvHeader = llNavHeader.findViewById(R.id.user_header);

        tabLayout = findViewById(R.id.home_activity_navigate);
        disableShiftMode(tabLayout);
        homeFragment = new HomeFragment();
        knowledgeFragment = new KnowledgeFragment();
        projectFragment = new ProjectFragment();
    }

    @Override
    public int setContextView() {
        return R.layout.activity_main;
    }

    long firstClick = 0L;

    private void initListener() {

        tabLayout.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        if (TAGFrag.equals("HomeFragment")){
                            smoothToTop(homeFragment);
                            break;
                        }
                        hiddenAllFragment();
                        addFragment(homeFragment, "HomeFragment");
                        break;
                    case R.id.navigation_official_accounts:
                        Intent intent = new Intent(MainActivity.this, OfficeAccountActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.navigation_dashboard:
                        if (TAGFrag.equals("KnowledgeFragment")){
                            smoothToTop(knowledgeFragment);
                            break;
                        }
                        hiddenAllFragment();
                        addFragment(knowledgeFragment, "KnowledgeFragment");
                        break;
                    case R.id.navigation_notifications:
                        if (TAGFrag.equals("ProjectFragment")){
                            break;
                        }
                        hiddenAllFragment();
                        addFragment(projectFragment, "ProjectFragment");
                        break;
                    default:
                        break;

                }
                return true;
            }
        });

        llNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!LoginUtil.getInstance().checkLogin()) {
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                }
            }
        });
    }

    private void hiddenAllFragment() {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        for (Fragment frag : getSupportFragmentManager().getFragments()) {
            transition.hide(frag);
        }
        transition.commit();
    }

    private void addFragment(Fragment frag, String tag) {
        FragmentTransaction transition = getSupportFragmentManager().beginTransaction();
        if (!getSupportFragmentManager().getFragments().contains(frag)) {
            transition.add(R.id.home_activity_content, frag, tag);
        } else {
            transition.show(frag);
        }
        TAGFrag = tag;
        transition.commit();
    }

    private void smoothToTop(FragmentInterface frag){
        long second = System.currentTimeMillis();
        Log.i("second",second - firstClick + "");
        if (second - firstClick < 1500) {
            homeFragment.smoothToTop();
        }else {
            firstClick = second;
        }
    }

    /**
     * Java反射得到BottomNavigationMenuView，关闭item >= 4 时的默认效果
     *
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void changeLogin(LoginEvent event) {
//        if (event.isHasSuccess()) {
//            tvUserName.setText(LoginUtil.getInstance().getLoginModel().getUsername());
//            tvUserEmail.setText(LoginUtil.getInstance().getLoginModel().getEmail());
//            tvHeader.setText(LoginUtil.getInstance().getLoginModel().getUsername().substring(0,1));
//        }
//    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_hot) {
            Intent intent = new Intent(this, HotActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_collect) {
            Intent intent = new Intent(MainActivity.this, CollectActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_login_out) {
            logout();

        } else if (id == R.id.nav_set) {
            startActivity(new Intent(this,SettingActivity.class));
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("showFrag", TAGFrag);
    }


    private void logout() {
        NetworkManager.getManager().getServer().logout()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    public void onSuccess(String s) {
//                        LoginUtil.getInstance().clearLoginInfo();
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
    }
}
