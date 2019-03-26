package lxy.com.wanandroid.project;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.knowledge.KnowledgeModel;
import lxy.com.wanandroid.officeAccount.OfficeAccountFragment;
import lxy.com.wanandroid.officeAccount.OfficeAccountModel;

/**
 * Creator : lxy
 * date: 2019/1/26
 */

public class ProjectViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<KnowledgeModel.DataBean> dataBeans = new ArrayList<>();
    private List<ItemFragment> fragments = new ArrayList<>();

    public ProjectViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setDataBeans(List<KnowledgeModel.DataBean> dataBeans) {
        this.dataBeans = dataBeans;
        for (KnowledgeModel.DataBean dataBean : dataBeans) {
            fragments.add(ItemFragment.newInstance(dataBean.getId()));
        }
        notifyDataSetChanged();
    }


    @Override
    public ItemFragment getItem(int position) {
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
