package lxy.com.wanandroid.officeAccount;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.FragmentInterface;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.detail.ArticleDetailActivity;
import lxy.com.wanandroid.detail.DetailModel;
import lxy.com.wanandroid.home.HomeAdapter;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.network.BaseObserver;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.utils.WaitDialog;

/**
 * Creator : lxy
 * date: 2019/3/18
 */

public class OfficeAccountFragment extends Fragment implements FragmentInterface{

    private RecyclerView rv;
    private HomeAdapter adapter;
    private List<ArticleModel> list = new ArrayList<>();
    private int ocId;
    private int page = 0;
    private int totalPages = 0;
    private SwipeRefreshLayout refreshLayout;
    private ViewDataBinding binding;
    private OfficeAccountViewModel viewModel;

    public static OfficeAccountFragment newInstance(int ocId) {

        Bundle args = new Bundle();
        args.putInt("officeAccountId",ocId);
        OfficeAccountFragment fragment = new OfficeAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_office_account, container, false);
        viewModel = ViewModelProviders.of(getActivity()).get(OfficeAccountViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null){
            ocId = getArguments().getInt("officeAccountId");
        }
        initView(view);
        initListener();
        getDataByServer();
    }

    private void initView(View view) {
        rv = view.findViewById(R.id.office_account_rv);
        adapter = new HomeAdapter(R.layout.item_home_article_image,list);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(adapter);
        refreshLayout = view.findViewById(R.id.office_account_swipe);
        refreshLayout.setColorSchemeColors(getContext().getResources().getColor(R.color.colorPrimary));
        
    }

    private void initListener(){
        refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getDataByServer();
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getContext(), ArticleDetailActivity.class);
                DetailModel model = new DetailModel();
                model.setName(list.get(position).getTitle());
                model.setLink(list.get(position).getLink());
                model.setId(list.get(position).getId());
                model.setCollect(list.get(position).isCollect());
                intent.putExtra("article",new Gson().toJson(model));
                startActivity(intent);
            }
        });

        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
                int total = adapter.getItemCount();
                if (lastVisibleItemPosition + Constants.ITEM_NUM >= total && page <= totalPages){
                    getDataByServer();
                }
            }
        });
    }

    private void getDataByServer(){
        NetworkManager.getManager().getServer().getOfficeAccountArticle(ocId,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<OfficeAccountArticleModel>() {
                    @Override
                    public void onSuccess(OfficeAccountArticleModel model) {
                        refreshLayout.setRefreshing(false);
                        if (page == 0){
                            list.clear();
                        }
                        list.addAll(model.getDatas());
                        totalPages = model.getPageCount();
                        adapter.notifyDataSetChanged();
                        page++;
                    }

                    @Override
                    public void onFailure(String message) {
                        refreshLayout.setRefreshing(false);
                    }
                });

    }

    @Override
    public void smoothToTop() {
        rv.scrollToPosition(0);
    }
}
