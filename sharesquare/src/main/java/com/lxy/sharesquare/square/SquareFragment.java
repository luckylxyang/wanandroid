package com.lxy.sharesquare.square;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.lxy.basemodel.base.Constants;
import com.lxy.basemodel.baseadapter.BaseAdapter;
import com.lxy.basemodel.detail.ArticleDetailActivity;
import com.lxy.basemodel.detail.DetailModel;
import com.lxy.basemodel.network.BaseApi;
import com.lxy.basemodel.network.BaseObserver;
import com.lxy.basemodel.network.NetworkManager;
import com.lxy.basemodel.network.model.ArticleModel;
import com.lxy.basemodel.network.model.PageModel;
import com.lxy.basemodel.network.model.ResponseModel;
import com.lxy.basemodel.utils.AnimatorUtils;
import com.lxy.basemodel.utils.ToastUtils;
import com.lxy.sharesquare.R;
import com.lxy.sharesquare.databinding.FragmentSquareBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class SquareFragment extends Fragment {

    private FragmentSquareBinding binding;
    private SquareViewModel viewModel;
    private SquareAdapter adapter;
    private List<ArticleModel> mList;
    private int page = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_square, container, false);
        viewModel = ViewModelProviders.of(this).get(SquareViewModel.class);
        binding.setViewModel(viewModel);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        initListener();
    }

    private void init() {
        mList = new ArrayList<>();
        adapter = new SquareAdapter(mList,R.layout.item_home_article);
        binding.squareRecycle.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.squareRecycle.setAdapter(adapter);
        binding.squareSwipe.setRefreshing(true);
        binding.squareSwipe.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        binding.squareSwipe.setOnRefreshListener(this::getArticle);
        getArticle();
    }

    private void initListener(){
        adapter.setOnItemListener((view, position) -> {
            Intent intent = new Intent(getContext(),ArticleDetailActivity.class);
            DetailModel model = new DetailModel();
            model.setId(mList.get(position).getId());
            model.setLink(mList.get(position).getLink());
            model.setName(mList.get(position).getTitle());
            model.setCollect(mList.get(position).isCollect());
            intent.putExtra("article",new Gson().toJson(model));
            startActivity(intent);
        });
        adapter.addOnItemChildClickListener((view, position) -> collectArticle(view, position));
    }

    private void getArticle(){
        viewModel.getSquareArticle(page).subscribe(new BaseObserver<PageModel<ArticleModel>>() {
            @Override
            public void onSuccess(PageModel<ArticleModel> articleModelPageModel) {
                binding.squareSwipe.setRefreshing(false);
                if (page == 0){
                    mList.clear();
                }
                mList.addAll(articleModelPageModel.getDatas());
                adapter.notifyDataSetChanged();
                page++;
            }

            @Override
            public void onFailure(String message) {
                binding.squareSwipe.setRefreshing(false);
                ToastUtils.show(message);
            }
        });
    }

    private void collectArticle(View view, int position){
        ArticleModel articleModel = mList.get(position);
        NetworkManager.getManager().getServer(BaseApi.class).collectArticleInSite(articleModel.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<PageModel<ArticleModel>>() {
                    @Override
                    public void onSuccess(PageModel<ArticleModel> articleModelPageModel) {
                        ToastUtils.show( R.string.collect_success);
                        AnimatorUtils.collect(view,()->{
                            articleModel.setCollect(true);
                            adapter.notifyItemChanged(position);
                        });
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.show(R.string.login_yet);
                        ARouter.getInstance().build(Constants.URL_LOGIN_ACTIVITY).navigation();
                    }
                });
    }
}
