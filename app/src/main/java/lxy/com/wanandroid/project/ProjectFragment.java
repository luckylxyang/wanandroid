package lxy.com.wanandroid.project;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lxy.basemodel.base.Constants;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.knowledge.KnowledgeModel;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * Creator : lxy
 * date: 2019/1/29
 */

public class ProjectFragment extends Fragment{

    private ProjectViewPagerAdapter adapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    /** 文章页数 */
    private int page = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frag_project,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        FragmentListener.getInstance().registerFragment(this);
        initView(view);
        initListener();
        getArticleByServer();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {

    }

    private void initView(View view) {
        adapter = new ProjectViewPagerAdapter(getChildFragmentManager());
        tabLayout = view.findViewById(R.id.project_tab);
        viewPager = view.findViewById(R.id.project_viewpager);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
    }

    public void getArticleByServer(){

        NetworkManager.getManager().getServer().getProjectTree()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<KnowledgeModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(KnowledgeModel model) {
                        if (model.getErrorCode() == Constants.NET_SUCCESS){
                            adapter.setDataBeans(model.getData());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(getContext(),e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
