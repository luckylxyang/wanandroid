package com.lxy.basemodel.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Creator : lxy
 * date: 2019/1/29
 */

public class DialogUtils {

    private static ProgressDialog progressDialog;

    public static void showProgress(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.show();
    }

    public static void hidden(){
        if (progressDialog != null && progressDialog.isShowing()){
            progressDialog.cancel();
        }
    }
}
