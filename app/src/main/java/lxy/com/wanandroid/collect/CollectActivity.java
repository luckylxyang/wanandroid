package lxy.com.wanandroid.collect;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;
import com.lxy.basemodel.base.BaseActivity;
import com.lxy.basemodel.base.Constants;
import com.lxy.basemodel.network.model.LoginEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import com.lxy.basemodel.detail.ArticleDetailActivity;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ToastUtils;
import com.lxy.basemodel.detail.DetailModel;
import lxy.com.wanandroid.network.NetworkManager;

public class CollectActivity extends BaseActivity {

    private RecyclerView recyclerView;
    private CollectAdapter articleAdapter;
    private List<CollectModel.DataBean.DatasBean> homeList;
    private int totalPage = 0;
    private SwipeRefreshLayout refreshLayout;
    private Disposable disposable;

    /** 文章页数 */
    private int page = 0;

    @Override
    public int setContextView() {
        return R.layout.frag_knowledge;
    }

    @Override
    protected void initOptions() {
        EventBus.getDefault().register(this);
        setToolbarTitle(getResources().getString(R.string.activity_collect));
        initView();
        initListener();
        getArticleByServer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeLogin(LoginEvent event){
        if (event.isHasSuccess()){
            getArticleByServer();
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initListener() {
        toolbar.setNavigationOnClickListener(v -> {
            if (disposable != null){
                disposable.dispose();
            }
            finish();
        });
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
        refreshLayout.setOnRefreshListener(() -> {
            page = 0;
            getArticleByServer();
        });
        articleAdapter.setOnItemListener((view, position) -> {
            Intent intent = new Intent(CollectActivity.this, ArticleDetailActivity.class);
            DetailModel model = new DetailModel();
            model.setId(homeList.get(position).getId());
            model.setLink(homeList.get(position).getLink());
            model.setName(homeList.get(position).getTitle());
            model.setCollect(true);
            intent.putExtra("article",new Gson().toJson(model));
            startActivity(intent);
        });
    }

    private void initView() {
        recyclerView = findViewById(R.id.knowledge_recycle);
        homeList = new ArrayList<>();
        articleAdapter = new CollectAdapter(this,homeList,R.layout.item_home_article);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(articleAdapter);
        refreshLayout = findViewById(R.id.knowledge_swipe);
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

    }

    public void getArticleByServer(){

        NetworkManager.getManager().getServer().getMyCollectList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CollectModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                    }

                    @Override
                    public void onNext(CollectModel model) {

                        refreshLayout.setRefreshing(false);
                        if (model.getErrorCode() == Constants.NET_SUCCESS){
                            if (page == 0){
                                homeList.clear();
                            }
                            homeList.addAll(model.getData().getDatas());
                            totalPage = model.getData().getPageCount();
                            ++page;
                            articleAdapter.notifyDataSetChanged();
                        }else if (model.getErrorCode() == -1001){
                            ToastUtils.show(model.getErrorMsg());
                            ARouter.getInstance().build(Constants.URL_LOGIN_ACTIVITY).navigation();
                        }
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
}
