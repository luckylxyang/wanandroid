package lxy.com.wanandroid.home.view;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

import com.google.gson.Gson;

import org.json.JSONObject;

import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.home.model.ArticleModel;

/**
 * date: 2019/1/25
 * @author lxy
 */

public class ArticleDetailActivity extends BaseActivity {

    private WebView webView;
    private String url;


    @Override
    protected void initOptions() {
        initView();
    }

    @Override
    public int setContextView() {
        return R.layout.detail_activity_acticle;
    }

    private void initView() {
        showToolbarBack(true);
        webView = findViewById(R.id.activity_detail_web);
        url = getIntent().getStringExtra("article");
        ArticleModel json = new Gson().fromJson(url, ArticleModel.class);

        webView.loadUrl(json.getLink());
    }


}
