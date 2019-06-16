package lxy.com.sdk.click;

import android.widget.RadioGroup;

/**
 * Creator : lxy
 * date: 2019/6/15
 */
public class WrapperRadioGroupOnCheckedChangeListener implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup.OnCheckedChangeListener listener;

    public WrapperRadioGroupOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (listener != null){
            listener.onCheckedChanged(group,checkedId);
        }
        SensorsDataClickPrivate.trackViewOnClick(group);
    }
}
