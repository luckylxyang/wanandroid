package lxy.com.wanandroid.officeAccount;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;

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

public class OfficeAccountActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    private List<OfficeAccountModel.DataBean> dataBeans = new ArrayList<>();
    @Override
    public int setContextView() {
        return R.layout.activity_office_account;
    }


    @Override
    protected void initOptions() {
        setToolbarTitle(getString(R.string.official_accounts));
        initView();
        getListByServer();
    }

    private void initView() {
        tabLayout = findViewById(R.id.office_account_tab);
        viewPager = findViewById(R.id.office_account_viewpager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager(),dataBeans);
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
                        adapter.notifyDataSetChanged();
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


}
