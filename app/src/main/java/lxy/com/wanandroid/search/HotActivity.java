package lxy.com.wanandroid.search;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lxy.basemodel.base.BaseActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.detail.ArticleDetailActivity;
import lxy.com.wanandroid.detail.DetailModel;
import lxy.com.wanandroid.knowledge.FlowLayout;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.utils.WaitDialog;

public class HotActivity extends BaseActivity {

    public static final String TAG = "HotActivity";
    private FlowLayout flWeb;

    @Override
    public int setContextView() {
        return R.layout.activity_hot;
    }

    @Override
    protected void initOptions() {
        setToolbarTitle(getString(R.string.hot_website));
        flWeb = findViewById(R.id.hot_website);
        getWebsites();
    }

    private void getWebsites() {
        WaitDialog.show(this);
        NetworkManager.getManager().getServer().getFriendWeb()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WebsiteModel>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(WebsiteModel websiteModel) {
                        if (websiteModel.getErrorCode() == 0){
                            LayoutInflater inflater = LayoutInflater.from(HotActivity.this);
                            for (WebsiteModel.DataBean bean : websiteModel.getData()) {
                                TextView tv = (TextView) inflater.inflate(R.layout.item_item_knowledge, flWeb, false);
                                tv.setText(bean.getName());
                                flWeb.addView(tv);
                                tv.setOnClickListener(v -> {
                                    Intent intent = new Intent(HotActivity.this,ArticleDetailActivity.class);
                                    DetailModel model = new DetailModel();
                                    model.setId(bean.getId());
                                    model.setLink(bean.getLink());
                                    model.setName(bean.getName());
                                    intent.putExtra("article",new Gson().toJson(model));
                                    startActivity(intent);
                                });
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.i(TAG,e.getMessage());
                        ToastUtils.show(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        WaitDialog.close();
                    }
                });
    }
}
