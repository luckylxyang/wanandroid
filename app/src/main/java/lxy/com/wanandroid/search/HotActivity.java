package lxy.com.wanandroid.search;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.ArticleDetailActivity;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.ResponseModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.home.HomeArticleAdapter;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.knowledge.FlowLayout;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * Creator : lxy
 * date: 2019/3/5
 */

public class HotActivity extends BaseActivity {

    private FlowLayout flowLayout;
    private SearchView mSearchView;
    private int page = 0;
    private int totalPage = 0;
    private FlowLayout historyLayout;
    private RecyclerView rvResult;
    private ScrollView svTag;
    private HomeArticleAdapter adapter;
    private List<ArticleModel> list = new ArrayList<>();


    @Override
    public int setContextView() {
        return R.layout.activit_hot;
    }

    @Override
    protected void initOptions() {
        setToolbarTitle(getString(R.string.action_tv_search));
        initView();
        getHotKeyByServer();
    }

    private void initView() {
        flowLayout = findViewById(R.id.hot_flow);
        historyLayout = findViewById(R.id.hot_flow_history);
        rvResult = findViewById(R.id.hot_recycler);
        svTag = findViewById(R.id.hot_scroll);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HomeArticleAdapter(this,list,R.layout.item_home_article);
        rvResult.setAdapter(adapter);
        svTag.setVisibility(View.VISIBLE);
        rvResult.setVisibility(View.GONE);

        adapter.setOnItemListener((view, position) -> {
            Intent intent = new Intent(HotActivity.this,ArticleDetailActivity.class);
            intent.putExtra("type",Constants.TYPE_ARTICLE);
            intent.putExtra("article",new Gson().toJson(list.get(position)));
            startActivity(intent);
        });
    }

    private void queryByServer(String query){

        NetworkManager.getManager().getServer().queryByKey(page,query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseModel model) {
                        svTag.setVisibility(View.GONE);
                        rvResult.setVisibility(View.VISIBLE);
                        if (model.getErrorCode() == Constants.NET_SUCCESS){
                            if (page == 0){
                                list.clear();
                            }
                            list.addAll(model.getData().getDatas());
                            adapter.notifyDataSetChanged();
                            totalPage = model.getData().getPageCount();
                            if (list.size() == 0){
                                ToastUtils.show(getString(R.string.search_result_no));
                            }
                        }else {
                            ToastUtils.show(model.getErrorMsg());
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

    private void getHotKeyByServer(){
        NetworkManager.getManager().getServer().getHotTag()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HotModel hotModel) {
                        if (hotModel.getErrorCode() == Constants.NET_SUCCESS){
                            displayHotKey(hotModel.getData());
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

    private void displayHotKey(List<HotModel.DataBean> data) {
        LayoutInflater inflater = LayoutInflater.from(this);
        for (HotModel.DataBean bean : data) {
            TextView tv = (TextView) inflater.inflate(R.layout.item_item_knowledge,flowLayout,false);
            tv.setText(bean.getName());
            flowLayout.addView(tv);
            tv.setOnClickListener(v -> mSearchView.setQuery(tv.getText(),true));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_search, menu);
        MenuItem compat = menu.findItem(R.id.hot_search);
        mSearchView = (SearchView) compat.getActionView();
        initSearchView();
        return super.onCreateOptionsMenu(menu);
    }

    private void initSearchView() {
        mSearchView.setQueryHint(getString(R.string.search_hot));
        mSearchView.setIconified(false);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                page = 0;
                queryByServer(query);
                saveSearchRecord(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        mSearchView.setOnCloseListener(() -> {
            rvResult.setVisibility(View.GONE);
            svTag.setVisibility(View.VISIBLE);
            return true;
        });
    }

    private void saveSearchRecord(String query) {

    }

}
