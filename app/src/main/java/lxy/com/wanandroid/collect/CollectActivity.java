package lxy.com.wanandroid.collect;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.home.HomeAdapter;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.network.NetworkManager;

public class CollectActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private CollectAdapter articleAdapter;
    private List<CollectModel.DataBean.DatasBean> homeList;
    private int totalPage = 0;
    private SwipeRefreshLayout refreshLayout;

    /** 文章页数 */
    private int page = 0;

    @Override
    public int setContextView() {
        return R.layout.frag_project;
    }

    @Override
    protected void initOptions() {
        setToolbarTitle(getResources().getString(R.string.activity_collect));
        initView();
        initListener();
        getArticleByServer();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {
        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                int total = articleAdapter.getItemCount();
                if (page <= totalPage && lastVisibleItemPosition + 4 >= total){
                    getArticleByServer();
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getArticleByServer();
            }
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.project_recycle);
        homeList = new ArrayList<>();
        articleAdapter = new CollectAdapter(this,homeList,R.layout.item_home_article);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(articleAdapter);
        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

    }

    public void getArticleByServer(){

        NetworkManager.getManager().getServer().getMyCollectList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(CollectModel model) {

                        refreshLayout.setRefreshing(false);
                        if (model.getErrorCode() == Constants.NET_SUCCESS){
                            if (page == 0){
                                homeList.clear();
                            }
                            homeList.addAll(model.getData().getDatas());
                            totalPage = model.getData().getPageCount();
                            ++page;
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            ToastUtils.show(model.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }
}
