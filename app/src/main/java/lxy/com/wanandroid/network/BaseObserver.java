package lxy.com.wanandroid.network;

import android.util.Log;

import com.lxy.basemodel.base.Constants;

import org.apache.http.conn.ConnectTimeoutException;

import java.net.SocketTimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by Administrator on 2019/11/8.
 */

public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {

    private static final String TAG = "BaseObserver";

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(BaseResponse<T> response) {
        if (Constants.NET_SUCCESS == response.getErrorCode()){
            onSuccess(response.getData());
        }else {
            Log.e(TAG,response.getErrorMsg());
            onFailure(response.getErrorMsg());
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        String emsg = "";
        if (e instanceof SocketTimeoutException){
            emsg = "从服务器获取响应数据超时";
        }else if (e instanceof ConnectTimeoutException){
            emsg = "与服务器建立连接超时";
        }else {
            emsg = e.getLocalizedMessage() + " 1";
        }
        onFailure(emsg);
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(T t);

    public abstract void onFailure(String message);
}
