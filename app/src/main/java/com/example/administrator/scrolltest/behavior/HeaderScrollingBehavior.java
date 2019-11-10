package com.example.administrator.scrolltest.behavior;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Scroller;

import com.example.administrator.scrolltest.R;

import java.lang.ref.WeakReference;

/**
 * Created by cyandev on 2016/11/3.
 * https://www.jianshu.com/p/7f50faa65622
 */
public class HeaderScrollingBehavior extends CoordinatorLayout.Behavior<RecyclerView> {
    public static final String TAG=HeaderScrollingBehavior.class.getCanonicalName();
    private boolean isExpanded = false;
    private boolean isScrolling = false;

    private WeakReference<View> dependentView;
    private Scroller scroller;
    private Handler handler;

    public HeaderScrollingBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        scroller = new Scroller(context);
        handler = new Handler();
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, RecyclerView child, View dependency) {
        //这里找出依赖视图的view
        if (dependency != null && dependency.getId() == R.id.scrolling_header) {
            dependentView = new WeakReference<>(dependency);
            return true;
        }
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, RecyclerView child, int layoutDirection) {
        //负责对被 Behavior 控制的视图进行布局
        CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
        if (lp.height == CoordinatorLayout.LayoutParams.MATCH_PARENT) {
            //改变recyclerView 高度，使其高度变为顶部收缩后 留给rv的最大高度
            child.layout(0, 0, parent.getWidth(), (int) (parent.getHeight() - getDependentViewCollapsedHeight()));
            return true;
        }

        return super.onLayoutChild(parent, child, layoutDirection);
    }
//setTranslationY 改变y属性移动view
    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, RecyclerView child, View dependency) {
       //根据依赖视图进行调整的方法，当依赖视图发生变化时，这个方法就会被调用
        //目标视图就是behavior属性写的view及RecyclerView
        Resources resources = getDependentView().getResources();
        final float progress = 1.f -
                Math.abs(dependency.getTranslationY() / (dependency.getHeight() - resources.getDimension(R.dimen.collapsed_header_height)));
        //控制rv的Y位置为 以来视图的高度（可能被改变，所以不固定）+以来视图移动的高度
        child.setTranslationY(dependency.getHeight() + dependency.getTranslationY());
        //改变依赖视图的大小
        float scale = 1 + 0.4f * (1.f - progress);
        dependency.setScaleX(scale);
        dependency.setScaleY(scale);
        //改变依赖视图的透明度
        dependency.setAlpha(progress);

        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View directTargetChild, View target, int nestedScrollAxes) {
        //用户按下手指时触发，询问 NSP 是否要处理这次滑动操作，
        //是否接受嵌套滚动,只有它返回true,后面的其他方法才会被调用
        //判断是竖直方向 true
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(CoordinatorLayout coordinatorLayout, RecyclerView child, View directTargetChild, View target, int nestedScrollAxes) {
        scroller.abortAnimation();
        isScrolling = false;

        super.onNestedScrollAccepted(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }
    //向上滑动时 先滑动外层，然后内层；向下滑动时先滑动内层 在滑动外层

    @Override
    public void onNestedPreScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target, int dx, int dy, int[] consumed) {
        //在内层view处理滚动事件前先被调用,可以让外层view先消耗部分滚动
       // Log.i(TAG, "onNestedPreScroll: "+dy);
        if (dy < 0) {
            //从上向下滑 dy为负值 即dy=dy0-dy1;
            //下滑时先不处理，等内层view处理了后再onNestedScroll内处理下滑剩余事件
            return;
        }
        //上滑事件，外层先处理，然后在内层处理

        View dependentView = getDependentView();
        //当前Y减去dy就是新的位置y
        float newTranslateY = dependentView.getTranslationY() - dy;
        //由于依赖view的Y都是<=0，所以最小Y需要的负值
        float minHeaderTranslate = -(dependentView.getHeight() - getDependentViewCollapsedHeight());

        if (newTranslateY > minHeaderTranslate) {
            //新Y没有滑到最小Y前 徐亚改变依赖view位置
            dependentView.setTranslationY(newTranslateY);
            consumed[1] = dy;//消费的距离
        }
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
       //在内层view将剩下的滚动消耗完之后调用,可以在这里处理最后剩下的滚动
        if (dyUnconsumed >0) {
            //上滑
            return;
        }
        //下滑时
        Log.i(TAG, "onNestedScroll: dyUnconsumed<=0 ");
        View dependentView = getDependentView();
        float newTranslateY = dependentView.getTranslationY() - dyUnconsumed;
        final float maxHeaderTranslate = 0;//依赖view的Y最大值设为0

        if (newTranslateY < maxHeaderTranslate) {
            dependentView.setTranslationY(newTranslateY);
        }
    }

    @Override
    public boolean onNestedPreFling(CoordinatorLayout coordinatorLayout, RecyclerView child, View target, float velocityX, float velocityY) {
       //在内层view的Fling事件处理之前被调用
        return onUserStopDragging(velocityY);
    }

    @Override
    public void onStopNestedScroll(CoordinatorLayout coordinatorLayout, RecyclerView child, View target) {
        //在内层view的Fling事件处理完之后调用
        if (!isScrolling) {
            onUserStopDragging(800);
        }
    }

    private boolean onUserStopDragging(float velocity) {
        View dependentView = getDependentView();
        float translateY = dependentView.getTranslationY();
        float minHeaderTranslate = -(dependentView.getHeight() - getDependentViewCollapsedHeight());

        if (translateY == 0 || translateY == minHeaderTranslate) {
            return false;
        }

        boolean targetState; // Flag indicates whether to expand the content.
        if (Math.abs(velocity) <= 800) {
            if (Math.abs(translateY) < Math.abs(translateY - minHeaderTranslate)) {
                targetState = false;
            } else {
                targetState = true;
            }
            velocity = 800; // Limit velocity's minimum value.
        } else {
            if (velocity > 0) {
                targetState = true;
            } else {
                targetState = false;
            }
        }

        float targetTranslateY = targetState ? minHeaderTranslate : 0;

        scroller.startScroll(0, (int) translateY, 0, (int) (targetTranslateY - translateY), (int) (1000000 / Math.abs(velocity)));
        handler.post(flingRunnable);
        isScrolling = true;
        return true;
    }

    private float getDependentViewCollapsedHeight() {
        //依赖的view的最小高度
        return getDependentView().getResources().getDimension(R.dimen.collapsed_header_height);
    }

    private View getDependentView() {
        return dependentView.get();
    }

    private Runnable flingRunnable = new Runnable() {
        @Override
        public void run() {
            if (scroller.computeScrollOffset()) {
                getDependentView().setTranslationY(scroller.getCurrY());
                handler.post(this);
            } else {
                isExpanded = getDependentView().getTranslationY() != 0;
                isScrolling = false;
            }
        }
    };

}
