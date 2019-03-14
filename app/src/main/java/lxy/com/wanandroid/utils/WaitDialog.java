package lxy.com.wanandroid.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import lxy.com.wanandroid.R;


public class WaitDialog extends Dialog {
	private Context context;
	private static WaitDialog waitDialog;

	public WaitDialog(Context context, boolean cancelable,
                      OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		this.context = context;
	}

	public WaitDialog(Context context, int theme) {
		super(context, theme);
		this.context = context;
	}

	public WaitDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = LayoutInflater.from(context).inflate(R.layout.wait_dialog,
				null);
		setContentView(v);
		setCanceledOnTouchOutside(false);
	}


	public static void show(Context context) {


		if (waitDialog == null) {
			waitDialog = new WaitDialog(context, R.style.wait_dialog);
		}else if(!waitDialog.getContext().equals(context)){
			if (waitDialog.isShowing()) {
				waitDialog.dismiss();
			}
			waitDialog = new WaitDialog(context, R.style.wait_dialog);
		}

		if (!waitDialog.isShowing()) {
 			waitDialog.show();
		}
	}

	public static void close() {
		if (waitDialog != null && waitDialog.isShowing()) {
			waitDialog.dismiss();
		}

	}
}
