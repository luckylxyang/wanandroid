package lxy.com.wanandroid.base;

import android.content.Context;
import android.widget.Toast;

/**
 * Creator : lxy
 * date: 2019/1/15
 */

public class ToastUtils {

//    private static ToastUtils instance;
    private static Toast toast;
//
//    private ToastUtils(){
//        toast = new Toast();
//    }
//
//    public static ToastUtils getInstance(){
//        if (instance == null){
//            instance = new ToastUtils();
//        }
//        return instance;
//    }

    public static void show(Context context,String message){
        if (toast == null){
            toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        }
        toast.cancel();
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(message);
        toast.show();
    }
}
