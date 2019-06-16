package lxy.com.sdk.click;

import android.view.View;
import android.widget.AdapterView;

/**
 * Creator : lxy
 * date: 2019/6/15
 */
public class WrapperAdapterViewOnItemSelectedListener implements AdapterView.OnItemSelectedListener {
    private AdapterView.OnItemSelectedListener listener;

    public WrapperAdapterViewOnItemSelectedListener(AdapterView.OnItemSelectedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (listener != null){
            listener.onItemSelected(parent,view,position,id);
        }
        SensorsDataClickPrivate.trackAdapterView(parent, view, position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if (listener != null){
            listener.onNothingSelected(parent);
        }
    }
}
