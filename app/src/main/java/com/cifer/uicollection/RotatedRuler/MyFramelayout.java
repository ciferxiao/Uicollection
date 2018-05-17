package com.cifer.uicollection.RotatedRuler;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by cifer
 * on 5/17/18.
 * 尺子的父布局，用于判断事件分发
 *
 */

public class MyFramelayout extends FrameLayout {
    private CircleRulerView mView;
    private boolean isOnRuler;

    public MyFramelayout(Context context) {
        this(context,null);
    }

    public MyFramelayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyFramelayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        isOnRuler = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("inter_bbd", "MyFrameLayout dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(mView == null){
            //mView = (CircleRulerView)this.findViewById(R.id.rcs_circleview); 获取尺子实例
        }

        Log.e("inter_bbd", "MyFrameLayout onInterceptTouchEvent" + "--->" + super.onInterceptTouchEvent(ev));
        float downX = ev.getRawX();
        float downY = ev.getRawY();

        Log.e("inter_bbd", "downX" + "--->" + downX + "  downY  ;---> " +downY);

        isOnRuler = mView.isOnRuler(downX,downY);

        Log.e("xiao_frame", "isOnRuler == " + isOnRuler);

        if (isOnRuler){
            Log.e("inter_bbd", "downX" + "---> return  false; " );
            return  false;
        }
        return true;
    }

}
