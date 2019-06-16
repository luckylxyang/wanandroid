package lxy.com.sdk.screen;

import android.support.v4.app.Fragment;

public class ListenerFragment extends Fragment {

    interface FragmentLifeCycle{
        void start();
        void stop();
        void destroy();
    }

    private FragmentLifeCycle lifeCycle;

    public void setFragmentLifeCycle(FragmentLifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
    }

    @Override
    public void onStart() {
        super.onStart();
        lifeCycle.start();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        lifeCycle.stop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifeCycle.destroy();
    }
}
