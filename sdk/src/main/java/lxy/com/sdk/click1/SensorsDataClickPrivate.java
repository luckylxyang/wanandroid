package lxy.com.sdk.click1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.v7.view.menu.ActionMenuItemView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import lxy.com.sdk.SensorsDataAPI;

/**
 * Creator : lxy
 * date: 2019/6/13
 */
public class SensorsDataClickPrivate {

    public static void trackViewOnClick(View v) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("element_type", v.getClass().getCanonicalName());
            jsonObject.put("element_id", getViewId(v));
            jsonObject.put("element_content", getElementContent(v));
            Activity activity = getActivityFromView(v);
            if (activity != null) {
                jsonObject.put("element_activity", activity.getClass().getCanonicalName());
            }
            SensorsDataAPI.getInstance().track("ViewClick", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void delegateViewsOnClickListener(Context context, View view) {
        if (context == null || view == null){
            return;
        }
        View.OnClickListener listener = getOnClickListener(view);
        if (listener != null && !(listener instanceof WrapperOnClickListener)){
            view.setOnClickListener(new WrapperOnClickListener(listener));
        }else if (view instanceof CompoundButton){
            CompoundButton.OnCheckedChangeListener checkChangeListener = getOnCheckChangeListener(view);
            if (checkChangeListener != null && !(checkChangeListener instanceof WrapperOnCheckedChangeListener)){
                ((CompoundButton) view).setOnCheckedChangeListener(new WrapperOnCheckedChangeListener(checkChangeListener));
            }
        }
        if (view instanceof ViewGroup){
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View child = viewGroup.getChildAt(i);
                delegateViewsOnClickListener(context, child);
            }
        }
    }

    private static CompoundButton.OnCheckedChangeListener getOnCheckChangeListener(View view) {
        try {
            Class viewClazz = Class.forName("android.widget.CompoundButton");
            Field declaredMethod = viewClazz.getDeclaredField("mOnCheckedChangeListener");
            if (!declaredMethod.isAccessible()) {
                declaredMethod.setAccessible(true);
            }
            return (CompoundButton.OnCheckedChangeListener) declaredMethod.get(view);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressLint("PrivateApi")
    public static View.OnClickListener getOnClickListener(View view) {
        boolean hasOnClick = view.hasOnClickListeners();
        if (hasOnClick) {
            try {
                Class viewClazz = Class.forName("android.view.View");
                Method declaredMethod = viewClazz.getDeclaredMethod("getListenerInfo");
                if (!declaredMethod.isAccessible()) {
                    declaredMethod.setAccessible(true);
                }
                Object invoke = declaredMethod.invoke(view);
                Class listenerInfoClazz = Class.forName("android.view.View$ListenerInfo");
                Field onClickListenerField = listenerInfoClazz.getDeclaredField("mOnClickListener");
                if (!onClickListenerField.isAccessible()){
                    onClickListenerField.setAccessible(true);
                }
                return (View.OnClickListener) onClickListenerField.get(invoke);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取 View 的 android:id 属性对应的字符串
     *
     * @param view
     * @return
     */
    public static String getViewId(View view) {
        String idStr = null;
        try {
            if (view.getId() != View.NO_ID) {
                idStr = view.getContext().getResources().getResourceEntryName(view.getId());
            }
        } catch (Exception e) {
            // ignore
        }
        return idStr;
    }

    /**
     * 获取 view 上显示的文本
     *
     * @param view
     * @return
     */
    public static String getElementContent(View view) {
        if (view == null) {
            return null;
        }
        String text = null;
        if (view instanceof Button) {
            text = ((Button) view).getText().toString();
        }else if (view instanceof TextView){
            text = ((TextView) view).getText().toString();
        }else if (view instanceof ImageView){
            text = view.getContentDescription().toString();
        }
        return text;
    }

    /**
     * 获取 view 所属的 activity
     *
     * @param view
     * @return
     */
    public static Activity getActivityFromView(View view) {
        Activity activity = null;
        if (view == null) {
            return activity;
        }
        try {
            Context context = view.getContext();
            if (context != null) {
                if (context instanceof Activity) {
                    activity = (Activity) context;
                } else if (context instanceof ContextWrapper) {
                    while (!(context instanceof Activity) && context instanceof ContextWrapper) {
                        context = ((ContextWrapper) context).getBaseContext();
                    }
                    if (context instanceof Activity) {
                        activity = (Activity) context;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activity;
    }

}
