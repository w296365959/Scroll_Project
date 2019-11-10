package com.example.administrator.scrolltest;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * (Hangzhou)
 *
 * @author: wzm
 * @date :  2019/3/22 16:55
 * Summary:
 */
public class InterceptScrollLinerLayout extends LinearLayout {
    public InterceptScrollLinerLayout(Context context) {
        this(context, null);
    }

    public InterceptScrollLinerLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterceptScrollLinerLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private float startX;
    private float startY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        //拦截TouchEvent
 /*       switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX=getX();
                startY=getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float difY = Math.abs(event.getRawY() - startY);
                float difX = Math.abs(event.getRawX() - startX);
                if (difY > 20 ||difY>difX  ) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }*/
        return true;
    }
}
