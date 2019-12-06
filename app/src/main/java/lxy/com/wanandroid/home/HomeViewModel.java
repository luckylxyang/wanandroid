package lxy.com.wanandroid.home;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

import lxy.com.wanandroid.base.BaseViewModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.home.model.BannerModel;
import lxy.com.wanandroid.network.BaseObserver;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.network.RxHelper;

/**
 * Created by Administrator on 2019/12/6.
 */

public class HomeViewModel extends BaseViewModel {

    public HomeViewModel(@NonNull Application application) {
        super(application);
    }

    private MutableLiveData<List<BannerModel>> getBannerList(){
        MutableLiveData<List<BannerModel>> liveData = new MutableLiveData<>();
        NetworkManager.getManager().getServer().getBannerList()
                .compose(RxHelper.observableIO2Main())
                .subscribe(new BaseObserver<List<BannerModel>>() {
                    @Override
                    public void onSuccess(List<BannerModel> model) {
                        Log.i("homeBanner",model.size() + " hjkkhlk" );
                        liveData.postValue(model);
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.show(message);
                    }
                });
        return liveData;
    }

}
