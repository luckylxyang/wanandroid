package lxy.com.wanandroid.home;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.base.FragmentInterface;
import lxy.com.wanandroid.detail.ArticleDetailActivity;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.detail.DetailModel;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.home.model.BannerModel;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * @author  : lxy
 * date: 2019/1/15
 */

public class HomeFragment extends Fragment implements FragmentInterface {


    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private HomeAdapter articleAdapter;
    private HomeMultiAdapter adapter;
    private List<ArticleModel> homeList;
    private int totalPage = 0;
    private Banner banner;
    private List<BannerModel.DataBean> bannerList;

    /** 文章页数 */
    private int page = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_frag_article,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
        refreshLayout.setRefreshing(true);
        getArticleByServer();
        getBannerByServer();
    }

    private void initView(View view) {
        bannerList = new ArrayList<>();
        banner = (Banner) getActivity().getLayoutInflater().inflate(R.layout.item_home_banner,null);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .isAutoPlay(true)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .setImageLoader(new GlideImageLoader());

        refreshLayout = view.findViewById(R.id.home_frag_refresh);
        recyclerView = view.findViewById(R.id.home_frag_recycle);
        homeList = new ArrayList<>();
        articleAdapter = new HomeAdapter(R.layout.item_home_article,homeList);
//        articleAdapter = new HomeArticleAdapter(getContext(),homeList,R.layout.item_home_article);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(articleAdapter);
        articleAdapter.setHeaderView(banner);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {
        articleAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(),ArticleDetailActivity.class);
                DetailModel model = new DetailModel();
                model.setId(homeList.get(position).getId());
                model.setLink(homeList.get(position).getLink());
                model.setName(homeList.get(position).getTitle());
                intent.putExtra("article",new Gson().toJson(model));
                startActivity(intent);
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 0;
                getArticleByServer();
            }
        });

        recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                int total = articleAdapter.getItemCount();
                if (lastVisibleItemPosition + Constants.ITEM_NUM >= total && page <= totalPage){
                    getArticleByServer();
                }
            }
        });

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getContext(),ArticleDetailActivity.class);
                DetailModel model = new DetailModel();
                model.setId(bannerList.get(position).getId());
                model.setLink(bannerList.get(position).getUrl());
                model.setName(bannerList.get(position).getTitle());
                intent.putExtra("article",new Gson().toJson(model));
                startActivity(intent);
            }
        });
    }

    public void getArticleByServer(){

        NetworkManager.getManager().getServer().getArticleList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseModel model) {
                        Log.i("homeArticle",model.getData().getSize() + " hjkkhlk" );
                        if (model.getErrorCode() == Constants.NET_SUCCESS){
                            if (page == 0){
                                homeList.clear();
                            }
                            homeList.addAll(model.getData().getDatas());
                            totalPage = model.getData().getPageCount();
                            ++page;
                            articleAdapter.notifyDataSetChanged();
                        }else {
                            refreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(e.getMessage());
                        refreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onComplete() {
                        refreshLayout.setRefreshing(false);
                    }
                });
    }

    public void getBannerByServer(){
        NetworkManager.getManager().getServer().getBannerList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BannerModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BannerModel model) {
                        Log.i("homeBanner",model.getData().size() + " hjkkhlk" );
                        bannerList.clear();
                        bannerList.addAll(model.getData());
                        banner.setImages(model.getData()).start();
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

    @Override
    public void smoothToTop() {
        recyclerView.scrollToPosition(0);
    }
}
