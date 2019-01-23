package lxy.com.wanandroid.home.view;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.home.HomeArticleAdapter;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * @author  : lxy
 * date: 2019/1/15
 */

public class HomeFragment extends Fragment {


    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private HomeArticleAdapter articleAdapter;
    private List<ArticleModel> homeList;
    private int totalPage = 0;

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
        getArticleByServer();
    }

    private void initView(View view) {
        refreshLayout = view.findViewById(R.id.home_frag_refresh);
        recyclerView = view.findViewById(R.id.home_frag_recycle);
        homeList = new ArrayList<>();
        articleAdapter = new HomeArticleAdapter(getContext(),homeList,R.layout.item_home_article);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(articleAdapter);

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
                        ToastUtils.show(getContext(),model.getErrorMsg());
                        Log.i("homeArticle",model.getData().getSize() + " hjkkhlk" );
                        if (model.getErrorCode() == Constants.NET_SUCCESS){
                            if (page == 0){
                                homeList.clear();
                            }
                            homeList.addAll(model.getData().getDatas());
                            totalPage = model.getData().getPageCount();
                            ++page;
                            articleAdapter.notifyDataSetChanged();
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
