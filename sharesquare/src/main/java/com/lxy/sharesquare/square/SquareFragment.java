package com.lxy.sharesquare.square;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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

import com.lxy.basemodel.network.BaseObserver;
import com.lxy.basemodel.network.model.ArticleModel;
import com.lxy.basemodel.network.model.PageModel;
import com.lxy.basemodel.utils.ToastUtils;
import com.lxy.sharesquare.R;
import com.lxy.sharesquare.databinding.FragmentSquareBinding;

import java.util.ArrayList;
import java.util.List;


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
}
