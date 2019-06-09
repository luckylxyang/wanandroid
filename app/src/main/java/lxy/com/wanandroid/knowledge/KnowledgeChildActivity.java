package lxy.com.wanandroid.knowledge;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.detail.ArticleDetailActivity;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.detail.DetailModel;
import lxy.com.wanandroid.home.HomeAdapter;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * Creator : lxy
 * date: 2019/2/27
 */

public class KnowledgeChildActivity extends BaseActivity{

    private RecyclerView rvKnowledge;
    private int page = 0;
    private String name;
    private int cid = 0;
    private HomeAdapter adapter;
    private List<ArticleModel> dataList;
    private int totalPage = 0;

    @Override
    public int setContextView() {
        return R.layout.activity_knowledge_child;
    }

    @Override
    protected void initOptions() {
        name = getIntent().getStringExtra("name");
        cid = getIntent().getIntExtra("id",0);
        setToolbarTitle(name);
        initView();
        initListener();
        getDataByServer();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {
        rvKnowledge.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            LinearLayoutManager manager = (LinearLayoutManager) rvKnowledge.getLayoutManager();
            int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
            int total = adapter.getItemCount();
            if (lastVisibleItemPosition + Constants.ITEM_NUM >= total && page <= totalPage){
                getDataByServer();
            }
        });

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Intent intent = new Intent(KnowledgeChildActivity.this, ArticleDetailActivity.class);
            DetailModel model = new DetailModel();
            model.setId(dataList.get(position).getId());
            model.setLink(dataList.get(position).getLink());
            model.setName(dataList.get(position).getTitle());
            intent.putExtra("article",new Gson().toJson(model));
            startActivity(intent);
        });
    }

    private void initView(){

        rvKnowledge = findViewById(R.id.knowledge_child_recycle);
        dataList = new ArrayList<>();
        adapter = new HomeAdapter(R.layout.item_home_article, dataList);
        rvKnowledge.setLayoutManager(new LinearLayoutManager(this));
        rvKnowledge.setAdapter(adapter);
    }

    private void getDataByServer(){
        NetworkManager.getManager().getServer().getKnowledgeList(page,cid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseModel model) {
                        if (model.getErrorCode() == Constants.NET_SUCCESS){
                            if (page == 0){
                                dataList.clear();
                            }
                            dataList.addAll(model.getData().getDatas());
                            totalPage = model.getData().getPageCount();
                            ++page;
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

}
