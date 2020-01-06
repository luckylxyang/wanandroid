package lxy.com.wanandroid.home;

import android.annotation.TargetApi;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
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

import com.google.gson.Gson;
import com.lxy.basemodel.base.Constants;
import com.lxy.basemodel.network.model.ArticleModel;
import com.lxy.basemodel.network.model.ResponseModel;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.base.FragmentInterface;
import lxy.com.wanandroid.detail.ArticleDetailActivity;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.detail.DetailModel;
import lxy.com.wanandroid.home.model.BannerModel;
import lxy.com.wanandroid.network.BaseObserver;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.network.RxHelper;

/**
 * @author  : lxy
 * date: 2019/1/15
 */

public class HomeFragment extends Fragment implements FragmentInterface {


    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private ArticleAdapter articleAdapter;
    private List<ArticleModel> homeList;
    private int totalPage = 0;
    private Banner banner;
    private List<BannerModel> bannerList;
    private ViewDataBinding binding;

    /** 文章页数 */
    private int page = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.home_frag_article, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
        refreshLayout.setRefreshing(true);
        getArticleByServer();
        getBannerByServer();
        initViewModel();
    }

    private void initViewModel() {
        HomeViewModel homeViewModel = new HomeViewModel(getActivity().getApplication());
        binding.setLifecycleOwner(this);
    }

    private void initView(View view) {

        refreshLayout = view.findViewById(R.id.home_frag_refresh);
        recyclerView = view.findViewById(R.id.home_frag_recycle);
        homeList = new ArrayList<>();
        articleAdapter = new ArticleAdapter(R.layout.item_home_article_image,homeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(articleAdapter);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        bannerList = new ArrayList<>();
        banner = (Banner) getActivity().getLayoutInflater().inflate(R.layout.item_home_banner,recyclerView,false);

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
                .isAutoPlay(true)
                .setIndicatorGravity(BannerConfig.RIGHT)
                .setImageLoader(new GlideImageLoader());

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {
        articleAdapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(getContext(),ArticleDetailActivity.class);
            DetailModel model = new DetailModel();
            model.setId(homeList.get(position).getId());
            model.setLink(homeList.get(position).getLink());
            model.setName(homeList.get(position).getTitle());
            model.setCollect(homeList.get(position).isCollect());
            intent.putExtra("article",new Gson().toJson(model));
            startActivity(intent);
        });

        refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getArticleByServer();
            getBannerByServer();
        });

        recyclerView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
            int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
            int total = articleAdapter.getItemCount();
            if (lastVisibleItemPosition + Constants.ITEM_NUM >= total && page <= totalPage){
                getArticleByServer();
            }
        });

        banner.setOnBannerListener(position -> {
            Intent intent = new Intent(getContext(),ArticleDetailActivity.class);
            DetailModel model = new DetailModel();
            model.setId(bannerList.get(position).getId());
            model.setLink(bannerList.get(position).getUrl());
            model.setName(bannerList.get(position).getTitle());
            intent.putExtra("article",new Gson().toJson(model));
            startActivity(intent);
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
                .compose(RxHelper.observableIO2Main())
                .subscribe(new BaseObserver<List<BannerModel>>() {
                    @Override
                    public void onSuccess(List<BannerModel> model) {
                        Log.i("homeBanner",model.size() + " hjkkhlk" );
                        bannerList.clear();
                        bannerList.addAll(model);
                        banner.setImages(model).start();
                        articleAdapter.addHeaderView(banner);
                        articleAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.show(message);
                    }
                });
    }

    @Override
    public void smoothToTop() {
        recyclerView.scrollToPosition(0);
    }
}
