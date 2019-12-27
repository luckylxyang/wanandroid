package lxy.com.wanandroid.officeAccount;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
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
import lxy.com.wanandroid.databinding.ActivityOfficeAccountBinding;
import lxy.com.wanandroid.network.BaseObserver;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.utils.WaitDialog;

/**
 * Creator : lxy
 * date: 2019/3/17
 */

public class OfficeAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPagerAdapter adapter;
    private List<OfficeAccountModel> dataBeans = new ArrayList<>();
    private int fragIndex;
    private ActivityOfficeAccountBinding binding;
    private ActivityOfficeAccountViewModel viewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_office_account);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_office_account);
        viewModel = ViewModelProviders.of(this).get(ActivityOfficeAccountViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        initView();
        initListener();
        getListByServer();
    }


    private void initListener() {
        binding.officeAccountFloat.setOnClickListener(this);
        binding.officeAccountViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        setSupportActionBar(binding.officeAccountToolbar);
        getSupportActionBar().setTitle(R.string.official_accounts);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.officeAccountToolbar.setNavigationOnClickListener(v -> finish());

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        binding.officeAccountViewpager.setAdapter(adapter);
        binding.officeAccountTab.setupWithViewPager(binding.officeAccountViewpager);
    }

    private void getListByServer() {
        viewModel.getListByServer().observe(this, officeAccountModels -> {
            dataBeans.clear();
            dataBeans.addAll(officeAccountModels);
            adapter.setDataBeans(dataBeans);
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.office_account_float:
                adapter.getItem(fragIndex).smoothToTop();
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

}
