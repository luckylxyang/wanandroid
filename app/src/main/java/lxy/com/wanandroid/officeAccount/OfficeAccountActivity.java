package lxy.com.wanandroid.officeAccount;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.utils.WaitDialog;

/**
 * Creator : lxy
 * date: 2019/3/17
 */

public class OfficeAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<OfficeAccountModel.DataBean> dataBeans = new ArrayList<>();
    private FloatingActionButton fabTop;
    private int fragIndex;
    private Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_account);

        initView();
        initListener();
        getListByServer();
    }


    private void initListener() {
        fabTop.setOnClickListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                fragIndex = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initView() {
        toolbar = findViewById(R.id.office_account_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.official_accounts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> finish());

        tabLayout = findViewById(R.id.office_account_tab);
        viewPager = findViewById(R.id.office_account_viewpager);
        fabTop = findViewById(R.id.office_account_float);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void getListByServer() {
        WaitDialog.show(this);
        NetworkManager.getManager().getServer().getOfficeAccountList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OfficeAccountModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(OfficeAccountModel model) {
                        if (model.getErrorCode() != 0){
                            ToastUtils.show(model.getErrorMsg());
                            return;
                        }
                        dataBeans.clear();
                        dataBeans.addAll(model.getData());
                        adapter.setDataBeans(dataBeans);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i("officeAccount",e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        WaitDialog.close();
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.office_account_float:
                adapter.getItem(fragIndex).smoothToTop();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.office_account_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
