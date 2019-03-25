package lxy.com.wanandroid.officeAccount;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Creator : lxy
 * date: 2019/1/26
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<OfficeAccountModel.DataBean> dataBeans = new ArrayList<>();
    private List<OfficeAccountFragment> fragments = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setDataBeans(List<OfficeAccountModel.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        for (OfficeAccountModel.DataBean dataBean : dataBeans) {
            fragments.add(OfficeAccountFragment.newInstance(dataBean.getId()));
        }
        notifyDataSetChanged();
    }


    @Override
    public OfficeAccountFragment getItem(int position) {
        return fragments.get(position);
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
