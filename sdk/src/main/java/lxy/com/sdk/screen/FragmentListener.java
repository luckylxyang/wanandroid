package lxy.com.sdk.screen;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Field;

import lxy.com.sdk.SensorsDataAPI;

/**
 * Creator : lxy
 * date: 2019/6/16
 */
public class FragmentListener {

    static final String LISTENER_FRAGMENT_TAG = "listener_fragment_tag";

    private ListenerFragment mListenerFragment;
    private Context mContext;

    private static FragmentListener instance;

    private FragmentListener(){}

    public static FragmentListener getInstance(){
        if (instance == null){
            instance = new FragmentListener();
        }
        return instance;
    }

    /**
     * Register fragment into FragmentListener
     * @param fragment
     */
    public void registerFragment(final Fragment fragment) {
        mContext = fragment.getActivity();
        if (mListenerFragment == null) {
            mListenerFragment = new ListenerFragment();
        }
        mListenerFragment.setFragmentLifeCycle(new ListenerFragment.FragmentLifeCycle() {

            @Override
            public void start() {
                getFragmentMessage(fragment, "start");
            }

            @Override
            public void stop() {
                getFragmentMessage(fragment, "stop");
            }

            @Override
            public void destroy() {
                getFragmentMessage(fragment, "destroy");
            }
        });
        // 由于Fragment的bug，必须将mChildFragmentManager的accessible设为true
        compatibleFragment(fragment);

        fragment.getChildFragmentManager()
                .beginTransaction()
                .add(mListenerFragment, LISTENER_FRAGMENT_TAG)
                .commitAllowingStateLoss();
    }

    private void getFragmentMessage(Fragment fragment, String lifeMethod){
        try {
            if (fragment == null) {
                return;
            }

            JSONObject object = new JSONObject();
            object.put("fragment_activity", fragment.getActivity().getClass().getCanonicalName());
            object.put("fragment", fragment.getClass().getCanonicalName());
            object.put("lifeMethod", lifeMethod);
            SensorsDataAPI.getInstance().track("AppViewScreen", object);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void compatibleFragment(Fragment fragment) {
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            if (!childFragmentManager.isAccessible()) {
                childFragmentManager.setAccessible(true);
            }
            childFragmentManager.set(fragment, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
