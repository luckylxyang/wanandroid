package lxy.com.wanandroid.knowledge;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Creator : lxy
 * date: 2019/2/26
 */

public class FlowLayoutManager extends RecyclerView.LayoutManager {
    private int dy;
    private SparseArray<Rect> allItemframs = new SparseArray<>();

    @Override
    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    int totalW = 0;
    int totalH = 0;
    private int mVerticalOffset;//竖直偏移量 每次换行时，要根据这个offset判断
    private int mFirstVisiPos;//屏幕可见的第一个View的Position
    private int mLastVisiPos;

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {

        if (getChildCount() == 0){
            detachAndScrapAttachedViews(recycler);
            return;
        }
        detachAndScrapAttachedViews(recycler);
        mVerticalOffset = 0;
        mFirstVisiPos = 0;
        mLastVisiPos = getItemCount();

        //初始化时调用 填充childView
//        fill(recycler, state);

        //开始摆放
        int offsetY = 0;
        int offsetX = 0;
        int viewH = 0;
        for (int i=0;i<getItemCount();i++) {
            View view = recycler.getViewForPosition(i);
            addView(view);//因为进行了detach操作，所以现在要重新添加
            measureChildWithMargins(view, 0, 0);//通知测量itemView

            int w=getDecoratedMeasuredWidth(view);//获取itemVIEW的实际大小，包括measureChildWithMargins方法中设置的大小
            int h=getDecoratedMeasuredHeight(view);
            viewH = h;
            Rect fram = allItemframs.get(i);
            if (fram == null){
                fram = new Rect();
            }
//            需要换行
            if (offsetX + w > getWidth()) {
//                换行的View的值
                offsetY += h;
                offsetX=w;
                fram.set(0, offsetY, w, offsetY + h);
            }else {
//                不需要换行
                fram.set(offsetX, offsetY, offsetX + w, offsetY + h);
                offsetX += w;
            }
//            要 针对于当前View   生成对应的Rect  然后放到allItemframs数组
            allItemframs.put(i, fram);
            layoutDecorated(view, fram.left, fram.top, fram.right, fram.bottom);
        }

//        int offsetTop = getPaddingTop();
//        int offsetLeft = getPaddingLeft();
//        for (int i = 0,size = getChildCount(); i < size; i++) {
//            View view = recycler.getViewForPosition(i);
//            addView(view);
//            measureChildWithMargins(view,0,0);
//
//            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
//            int childW = getDecoratedMeasuredWidth(view) + params.leftMargin + params.rightMargin;
//            int childH = getDecoratedMeasuredHeight(view) + params.topMargin + params.bottomMargin;
//            if (totalW + childW > getWidth() - getPaddingLeft() - getPaddingRight()){
//                offsetLeft = getPaddingLeft();
//                offsetTop += totalH;
//                totalH += childH;
//                totalW = childW;
//                layoutDecoratedWithMargins(view,offsetLeft,offsetTop,offsetLeft + totalW,offsetTop + totalH);
//
//            }else {
//                totalW += childW;
//                totalH = Math.max(totalH,childH);
//                layoutDecoratedWithMargins(view,offsetLeft,offsetTop,offsetLeft + childW,offsetTop + childH);
//
//            }
//        }
    }

    private void fill(RecyclerView.Recycler recycler,RecyclerView.State state){
        int topOffset = getPaddingTop();//布局时的上偏移
        int leftOffset = getPaddingLeft();//布局时的左偏移
        int lineMaxHeight = 0;//每一行最大的高度
        int minPos = mFirstVisiPos;//初始化时，我们不清楚究竟要layout多少个子View，所以就假设从0~itemcount-1
        mLastVisiPos = getItemCount() - 1;
        //顺序addChildView
        for (int i = minPos; i <= mLastVisiPos; i++) {
            //找recycler要一个childItemView,我们不管它是从scrap里取，还是从RecyclerViewPool里取，亦或是onCreateViewHolder里拿。
            View child = recycler.getViewForPosition(i);
            addView(child);
            measureChildWithMargins(child, 0, 0);
            //计算宽度 包括margin
            if (leftOffset + getDecoratedMeasurementHorizontal(child) <= getHorizontalSpace()) {//当前行还排列的下
                layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + getDecoratedMeasurementHorizontal(child), topOffset + getDecoratedMeasurementVertical(child));

                //改变 left  lineHeight
                leftOffset += getDecoratedMeasurementHorizontal(child);
                lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(child));
            } else {//当前行排列不下
                //改变top  left  lineHeight
                leftOffset = getPaddingLeft();
                topOffset += lineMaxHeight;
                lineMaxHeight = 0;

                //新起一行的时候要判断一下边界
                if (topOffset - dy > getHeight() - getPaddingBottom()) {
                    //越界了 就回收
                    removeAndRecycleView(child, recycler);
                    mLastVisiPos = i - 1;
                } else {
                    layoutDecoratedWithMargins(child, leftOffset, topOffset, leftOffset + getDecoratedMeasurementHorizontal(child), topOffset + getDecoratedMeasurementVertical(child));

                    //改变 left  lineHeight
                    leftOffset += getDecoratedMeasurementHorizontal(child);
                    lineMaxHeight = Math.max(lineMaxHeight, getDecoratedMeasurementVertical(child));
                }
            }
        }
    }

    /**
     * 获取某个childView在水平方向所占的空间
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementHorizontal(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredWidth(view) + params.leftMargin
                + params.rightMargin;
    }

    /**
     * 获取某个childView在竖直方向所占的空间
     *
     * @param view
     * @return
     */
    public int getDecoratedMeasurementVertical(View view) {
        final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)
                view.getLayoutParams();
        return getDecoratedMeasuredHeight(view) + params.topMargin
                + params.bottomMargin;
    }

    public int getVerticalSpace() {
        return getHeight() - getPaddingTop() - getPaddingBottom();
    }

    public int getHorizontalSpace() {
        return getWidth() - getPaddingLeft() - getPaddingRight();
    }
}
