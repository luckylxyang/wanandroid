package lxy.com.sdk.click;

import android.widget.CompoundButton;

/**
 * Creator : lxy
 * date: 2019/6/13
 */
public class WrapperOnCheckedChangeListener implements CompoundButton.OnCheckedChangeListener {
    private CompoundButton.OnCheckedChangeListener listener;

    public WrapperOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (listener != null){
            listener.onCheckedChanged(buttonView,isChecked);
        }
        SensorsDataClickPrivate.trackViewOnClick(buttonView);
    }
}
