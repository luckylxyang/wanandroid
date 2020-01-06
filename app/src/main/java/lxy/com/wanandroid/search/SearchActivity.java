package lxy.com.wanandroid.search;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lxy.basemodel.base.BaseActivity;
import com.lxy.basemodel.base.Constants;
import com.lxy.basemodel.network.model.ArticleModel;
import com.lxy.basemodel.network.model.ResponseModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.detail.ArticleDetailActivity;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.db.DBHelper;
import lxy.com.wanandroid.detail.DetailModel;
import lxy.com.wanandroid.greendao.SearchHistoryModelDao;
import lxy.com.wanandroid.home.ArticleAdapter;
import lxy.com.wanandroid.knowledge.FlowLayout;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.utils.WaitDialog;

/**
 * Creator : lxy
 * date: 2019/3/5
 */

public class SearchActivity extends BaseActivity {

    private FlowLayout flowLayout;
    private SearchView mSearchView;
    private int page = 0;
    private int totalPage = 0;
    private FlowLayout historyLayout;
    private RecyclerView rvResult;
    private ScrollView svTag;
    private ArticleAdapter adapter;
    private List<ArticleModel> list = new ArrayList<>();


    @Override
    public int setContextView() {
        return R.layout.activit_search;
    }


    @Override
    protected void initOptions() {
        setToolbarTitle(getString(R.string.action_tv_search));
        initView();
        initListener();
        getHotKeyByServer();
        querySearchHistory();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {
        rvResult.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            LinearLayoutManager manager = (LinearLayoutManager) rvResult.getLayoutManager();
            int lastVisibleItemPosition = manager.findLastVisibleItemPosition();
            int total = adapter.getItemCount();
            if (lastVisibleItemPosition + Constants.ITEM_NUM >= total && page <= totalPage) {
                queryByServer(mSearchView.getQuery().toString());
            }
        });

    }

    private void initView() {
        flowLayout = findViewById(R.id.hot_flow);
        historyLayout = findViewById(R.id.hot_flow_history);
        rvResult = findViewById(R.id.hot_recycler);
        svTag = findViewById(R.id.hot_scroll);
        rvResult.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArticleAdapter(R.layout.item_home_article_image, list);
        rvResult.setAdapter(adapter);
        adapter.bindToRecyclerView(rvResult);
        adapter.setEmptyView(R.layout.item_empty);
        svTag.setVisibility(View.VISIBLE);
        rvResult.setVisibility(View.GONE);

        adapter.setOnItemClickListener(((adapter1, view, position) -> {
            Intent intent = new Intent(SearchActivity.this, ArticleDetailActivity.class);
            DetailModel model = new DetailModel();
            model.setId(list.get(position).getId());
            model.setLink(list.get(position).getLink());
            model.setName(list.get(position).getTitle());
            model.setCollect(list.get(position).isCollect());
            intent.putExtra("article",new Gson().toJson(model));
            startActivity(intent);
        }));
    }

    private void queryByServer(String query) {

        WaitDialog.show(this);
        NetworkManager.getManager().getServer().queryByKey(page, query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResponseModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseModel model) {
                        svTag.setVisibility(View.GONE);
                        if (rvResult.getVisibility() != View.VISIBLE) {
                            rvResult.setVisibility(View.VISIBLE);
                            animatorIn();
                        }
                        if (model.getErrorCode() == Constants.NET_SUCCESS) {
                            if (page == 0) {
                                list.clear();
                            }
                            list.addAll(model.getData().getDatas());
                            adapter.notifyDataSetChanged();
                            totalPage = model.getData().getPageCount();
                            page++;
                            if (list.size() == 0) {
                                ToastUtils.show(getString(R.string.search_result_no));
                            }
                            rvResult.scrollToPosition(0);
                        } else {
                            ToastUtils.show(model.getErrorMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        WaitDialog.close();
                    }
                });
    }

    private void getHotKeyByServer() {
        NetworkManager.getManager().getServer().getHotTag()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HotModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HotModel hotModel) {
                        if (hotModel.getErrorCode() == Constants.NET_SUCCESS) {
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
            TextView tv = (TextView) inflater.inflate(R.layout.item_item_knowledge, flowLayout, false);
            tv.setText(bean.getName());
            flowLayout.addView(tv);
            tv.setOnClickListener(v -> {
                mSearchView.setQuery(tv.getText(), true);
                mSearchView.clearFocus();
            });
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
            animatorOut();
            svTag.setVisibility(View.VISIBLE);
            querySearchHistory();
            mSearchView.setFocusable(false);
            mSearchView.clearFocus();
//            closeSoftInput();
            return true;
        });
    }

    private void closeSoftInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void saveSearchRecord(String query) {
        try {
            SearchHistoryModelDao dao = DBHelper.getInstance().getDaoSession().getSearchHistoryModelDao();
            List<SearchHistoryModel> list = dao.queryBuilder().where(SearchHistoryModelDao.Properties.Content.eq(query)).list();
            if (list != null && list.size() != 0) {
                list.get(0).setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                dao.update(list.get(0));
            } else {
                SearchHistoryModel model = new SearchHistoryModel();
                model.setContent(query);
                model.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date()));
                dao.insertOrReplace(model);
            }
        } catch (Exception e) {
            Log.e("insert search", e.getMessage());
        }
    }

    private void querySearchHistory() {
        List<SearchHistoryModel> data = DBHelper.getInstance().getDaoSession().getSearchHistoryModelDao()
                .queryBuilder()
                .orderDesc(SearchHistoryModelDao.Properties.Date)
                .list();
        historyLayout.removeAllViews();
        if (data == null) {
            return;
        }
        LayoutInflater inflater = LayoutInflater.from(this);
        for (SearchHistoryModel bean : data) {
            TextView tv = (TextView) inflater.inflate(R.layout.item_item_knowledge, historyLayout, false);
            tv.setText(bean.getContent());
            historyLayout.addView(tv);
            tv.setOnClickListener(v -> {
                mSearchView.setQuery(tv.getText(), true);
                mSearchView.clearFocus();
            });
        }
    }

    /**
     * 搜索结果列表的入场动画
     */
    private void animatorIn() {

        float height = getWindowManager().getDefaultDisplay().getHeight();
        ObjectAnimator animator = ObjectAnimator.ofFloat(rvResult, "translationY", height, 0);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(rvResult, "alpha", 0f, 1f);

        AnimatorSet set = new AnimatorSet();
        set.play(animator).with(animator2);
        set.setDuration(500);
        set.start();

    }

    /**
     * 搜索结果列表的退出动画
     */
    private void animatorOut() {
        float height = getWindowManager().getDefaultDisplay().getHeight();
        ObjectAnimator animator = ObjectAnimator.ofFloat(rvResult, "translationY", 0f, height);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(rvResult, "alpha", 1f, 0f);

        AnimatorSet set = new AnimatorSet();
        set.play(animator).with(animator2);
        set.setDuration(500);
        set.start();
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                rvResult.setVisibility(View.GONE);
            }
        });
    }
}
