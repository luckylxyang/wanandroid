package lxy.com.wanandroid.officeAccount;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Creator : lxy
 * date: 2019/1/26
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<OfficeAccountModel.DataBean> dataBeans;

    public ViewPagerAdapter(FragmentManager fm, List<OfficeAccountModel.DataBean> dataBeans) {
        super(fm);
        this.dataBeans = dataBeans;
    }

    @Override
    public OfficeAccountFragment getItem(int position) {
        return OfficeAccountFragment.newInstance(dataBeans.get(position).getId());
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return dataBeans.get(position).getName();
    }

    @Override
    public int getCount() {
        return dataBeans.size();
    }
}
