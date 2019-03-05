package lxy.com.wanandroid.search;

import android.view.LayoutInflater;
import android.widget.TextView;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.R;
import lxy.com.wanandroid.base.BaseActivity;
import lxy.com.wanandroid.base.Constants;
import lxy.com.wanandroid.knowledge.FlowLayout;
import lxy.com.wanandroid.network.NetworkManager;

/**
 * Creator : lxy
 * date: 2019/3/5
 */

public class HotActivity extends BaseActivity {

    private FlowLayout flowLayout;


    @Override
    public int setContextView() {
        return R.layout.activit_hot;
    }

    @Override
    protected void initOptions() {
        initView();
        getHotKeyByServer();
    }

    private void initView() {
        flowLayout = findViewById(R.id.hot_flow);

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
        }
    }
}
