package lxy.com.wanandroid.project;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import lxy.com.wanandroid.home.HomeAdapter;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * Creator : lxy
 * date: 2019/1/29
 */

public class ProjectFragment extends Fragment {

    private RecyclerView recyclerView;
    private HomeAdapter articleAdapter;
    private List<ArticleModel> homeList;
    private int totalPage = 0;

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
        initView(view);
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
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.project_recycle);
        homeList = new ArrayList<>();
        articleAdapter = new HomeAdapter(R.layout.item_home_article,homeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(articleAdapter);

    }

    public void getArticleByServer(){

        NetworkManager.getManager().getServer().getNewProject(page)
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
