package lxy.com.wanandroid;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.gson.Gson;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONObject;

import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.base.SwipeBackActivity;
import lxy.com.wanandroid.home.model.ArticleModel;
import lxy.com.wanandroid.home.model.BannerModel;

/**
 * date: 2019/1/25
 *
 * @author lxy
 */

public class ArticleDetailActivity extends SwipeBackActivity {

    private WebView webView;
    private String url;
    private AVLoadingIndicatorView loadingView;
    private ArticleModel model;
    private BannerModel.DataBean bannerModel;
    private BottomSheetDialog sheetDialog;
    private ImageView ivError;
    private boolean isError = false;
    private BottomSheetBehavior behavior;
    private View decorView;


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
        decorView = getWindow().getDecorView();
        showToolbarBack(true);
        webView = findViewById(R.id.activity_detail_web);
        loadingView = findViewById(R.id.detail_activity_loading);
        String type = getIntent().getStringExtra("type");
        url = getIntent().getStringExtra("article");
        if (Constants.TYPE_ARTICLE.equals(type)){
            model = new Gson().fromJson(url, ArticleModel.class);
            getSupportActionBar().setTitle(model.getTitle());
            webView.loadUrl(model.getLink());
            if (model.isCollect()){
                ((TextView)toolbar.findViewById(R.id.detail_love)).setText("取消收藏");
            }
        }else {
            bannerModel = new Gson().fromJson(url,BannerModel.DataBean.class);
            getSupportActionBar().setTitle(bannerModel.getTitle());
            webView.loadUrl(bannerModel.getUrl());
        }
        ivError = findViewById(R.id.detail_activity_error);
        initWebView();
        initDialog();

    }

    private void initDialog() {
        sheetDialog = new BottomSheetDialog(this);
        View view = View.inflate(this,R.layout.detail_bottom_sheet,null);
        sheetDialog.setContentView(view);
        behavior = BottomSheetBehavior.from(view.findViewById(R.id.bottom_sheet));
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        sheetDialog.show();

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
                    isError = false;
                    loadingView.show();
                    webView.reload();
                    break;
                case R.id.detail_dot:

                    if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN){
                        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    }else if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                    }

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
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().getPath());
                webView.setVisibility(View.VISIBLE);
                loadingView.show();
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                loadingView.show();
                ivError.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingView.hide();
                if (isError){
                    ivError.setVisibility(View.VISIBLE);
                    view.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isError = true;

            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);
                isError = true;

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbar.inflateMenu(R.menu.detail_menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN){
            if (webView.canGoBack()){
                webView.goBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    float startX = 0f;
    float startY = 0f;
    boolean canFinish = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startX = event.getX();
                startY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float subX = event.getX() - startX;
                if (subX > 0) {
                    decorView.setX(subX);
                }
                isBack(event);
                break;
            case MotionEvent.ACTION_UP:
                if (canFinish){
                    finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void isBack(MotionEvent event){
        float x = event.getX();
        float y = event.getY();

        if (startX > 100){
            return ;
        }
        if (Math.abs(startY - y) > 100){
            return ;
        }
        DisplayMetrics outMe = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMe);
        int width = outMe.widthPixels;
        if (x - startX > width / 3){
            canFinish = true;
        }
        return ;
    }
}
