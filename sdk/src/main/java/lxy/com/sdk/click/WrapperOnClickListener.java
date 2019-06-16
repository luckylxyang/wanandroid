package lxy.com.sdk.click;

import android.view.View;

/**
 * Creator : lxy
 * date: 2019/6/13
 */
public class WrapperOnClickListener implements View.OnClickListener {

    private View.OnClickListener source;

    public WrapperOnClickListener(View.OnClickListener source) {
        this.source = source;
    }

    @Override
    public void onClick(View v) {
        try {
            if (source != null){
                source.onClick(v);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        SensorsDataClickPrivate.trackViewOnClick(v);
    }
}
