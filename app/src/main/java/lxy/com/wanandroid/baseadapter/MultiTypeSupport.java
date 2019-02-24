package lxy.com.wanandroid.baseadapter;

/**
 * date: 2019/2/8
 * @author lxy
 */

public interface MultiTypeSupport<T> {

    /**
     * 获取多列表布局
     * @param itemType
     * @return
     */
    int getLayoutId(int itemType);

    int getItemViewType(int position,T t);
}
