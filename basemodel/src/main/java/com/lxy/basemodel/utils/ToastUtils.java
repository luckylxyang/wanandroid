package com.lxy.basemodel.utils;

import android.content.Context;
import android.widget.Toast;

import com.lxy.basemodel.BaseApp;


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

    public static void show(String message){
        if (toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(BaseApp.getContext(),message,Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(Context context,String message){
        if (toast != null){
            toast.cancel();
        }
        toast = Toast.makeText(context,message,Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void show(int strId){
        show(BaseApp.getContext().getString(strId));
    }

    public static void show(Context context,int strId){
        show(context,BaseApp.getContext().getString(strId));
    }
}
