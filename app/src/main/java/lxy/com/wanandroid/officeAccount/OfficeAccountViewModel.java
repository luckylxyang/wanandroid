package lxy.com.wanandroid.officeAccount;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import lxy.com.wanandroid.base.BaseViewModel;
import lxy.com.wanandroid.base.ToastUtils;
import lxy.com.wanandroid.network.BaseObserver;
import lxy.com.wanandroid.network.NetworkManager;
import lxy.com.wanandroid.utils.WaitDialog;

/**
 * Created by Administrator on 2019/12/6.
 */

public class OfficeAccountViewModel extends BaseViewModel {

    public OfficeAccountViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<OfficeAccountModel>> getListByServer() {
        MutableLiveData<List<OfficeAccountModel>> liveData = new MutableLiveData<>();
        NetworkManager.getManager().getServer().getOfficeAccountList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<List<OfficeAccountModel>>() {
                    @Override
                    public void onSuccess(List<OfficeAccountModel> model) {
                        liveData.setValue(model);
                    }

                    @Override
                    public void onFailure(String message) {
                        ToastUtils.show(message);
                    }
                });
        return liveData;
    }

    public MutableLiveData<OfficeAccountArticleModel> getOfficeAccountArticle(int officeAccountId,int pageIndex){
        MutableLiveData<OfficeAccountArticleModel> liveData = new MutableLiveData<>();
        NetworkManager.getManager().getServer().getOfficeAccountArticle(officeAccountId,pageIndex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<OfficeAccountArticleModel>() {
                    @Override
                    public void onSuccess(OfficeAccountArticleModel model) {
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
