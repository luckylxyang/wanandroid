package lxy.com.wanandroid;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.home.model.BannerModel;
import lxy.com.wanandroid.utils.DialogUtils;

/**
 * date: 2019/1/25
 *
 * @author lxy
 */

public class ArticleDetailActivity extends BaseActivity {

    private WebView webView;
    private String url;
    private AVLoadingIndicatorView loadingView;
    private ArticleModel model;
    private BannerModel.DataBean bannerModel;


    @Override
    protected void initOptions() {
        initView();
        initListener();
    }


    @Override
    public int setContextView() {
        return R.layout.detail_activity_acticle;
    }

    private void initView() {
        showToolbarBack(true);
        webView = findViewById(R.id.activity_detail_web);
        loadingView = findViewById(R.id.detail_activity_loading);
        String type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("article");
        if (Constants.TYPE_ARTICLE.equals(type)){
            model = new Gson().fromJson(url, ArticleModel.class);
            getSupportActionBar().setTitle(model.getTitle());
            webView.loadUrl(model.getLink());
        }else {
            bannerModel = new Gson().fromJson(url,BannerModel.DataBean.class);
            getSupportActionBar().setTitle(bannerModel.getTitle());
            webView.loadUrl(bannerModel.getUrl());
        }


        initWebView();
    }

    private void initListener() {
        toolbar.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.detail_love:

                    break;
                case R.id.detail_share:
                    break;
                case R.id.detail_other:
                    Uri uri = Uri.parse(model.getLink());
                    Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                    break;
                case R.id.detail_refresh:
                    refreshWebPage();
                    webView.reload();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void refreshWebPage() {
        View view = toolbar.findViewById(R.id.detail_refresh);
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,359);
        animator.setDuration(500);
        animator.start();
    }


    private void initWebView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().getPath());
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingView.show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingView.hide();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.detail_menu);
        return super.onCreateOptionsMenu(menu);
    }
}
