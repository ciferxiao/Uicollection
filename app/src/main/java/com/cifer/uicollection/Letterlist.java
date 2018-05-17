package com.cifer.uicollection;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by CIFER on 4/9/18.
 *
 */

public class Letterlist extends View{

    private String letters[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
            "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "#"};
    private float letterheight ;
    private int letterwidth;
    private Paint letterpaint;
    /*手指按下的字母索引*/
    private Paint bgPaint;
    private int currentindex;
    boolean iscurrenttouch ;
    private int bgcolor;
    private float lettersize;


    public Letterlist(Context context) {
        this(context,null);
        Log.d("xiao","11111111");
    }

    public Letterlist(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        Log.d("xiao","22222222");
    }

    public Letterlist(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.d("xiao","333333333");
        initview(attrs);
    }

    private void initview(AttributeSet attrs){
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs,R.styleable.app);
        bgcolor = typedArray.getColor(R.styleable.app_bgcolor,Color.BLUE);
        lettersize = typedArray.getDimension(R.styleable.app_textSize,35f);
        typedArray.recycle();


        letterpaint = new Paint();
        letterpaint.setAntiAlias(true);
        letterpaint.setTextSize(lettersize);

        bgPaint = new Paint();
        bgPaint.setAntiAlias(true);
        bgPaint.setColor(bgcolor);

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        letterheight = getMeasuredHeight()/letters.length;
        letterwidth = getMeasuredWidth() ;
        Log.d("xiao","widthMeasureSpec == " + widthMeasureSpec + "  heightMeasureSpec ==" + heightMeasureSpec);
        Log.d("xiao","getMeasuredHeight == " + getMeasuredHeight() + "  getMeasuredWidth ==" + getMeasuredWidth());

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
            for (int i = 0; i < letters.length; i++) {
                //获取文字的宽高  plan 1:
                /*Rect rect = new Rect();
                letterpaint.getTextBounds(letters[i], 0, 1, rect);
                int wordWidth = rect.width();
                float wordX = letterwidth / 2 - wordWidth / 2;
                float wordY = letterwidth / 2 + i * letterheight;
                //绘制字母
                canvas.drawText(letters[i], wordX, wordY, letterpaint);*/
                //plan 2:
                float letterwidth = letterpaint.measureText(letters[i]);
                int contentwidth = getWidth() - getPaddingLeft() - getPaddingRight();
                float startX = getPaddingLeft() + (contentwidth - letterwidth)/2;
                Paint.FontMetrics fontMetrics = letterpaint.getFontMetrics();
                float baseLine = letterheight / 2 + (letterheight * i) + (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom;
                if (currentindex == i) {
                    //绘制文字圆形背景
                    canvas.drawCircle(startX + letterwidth/2 , letterheight / 2 + i * letterheight, 23, bgPaint);
                    letterpaint.setColor(Color.WHITE);
                } else {
                    letterpaint.setColor(Color.GRAY);
                }
                canvas.drawText(letters[i],startX,baseLine,letterpaint);
            }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                float eventY = event.getY();
                iscurrenttouch = true ;
                int index = (int)(eventY / letterheight);
                if(currentindex == index){
                    return true;
                }else{
                    currentindex = index;
                }
                Log.i("xiao","currentindex == " + currentindex);
                if(listener != null && 0<= currentindex && currentindex < letters.length){
                    listener.onTouch(letters[currentindex],iscurrenttouch);
                }
                invalidate();
                break;
/*            case MotionEvent.ACTION_UP:
                iscurrenttouch = false ;
                if(listener != null){
                    listener.onTouch(letters[currentindex],iscurrenttouch);
                }
                break;*/
        }
        return true;
    }


    public void setonLetterTouchListener(onLetterClickListener letterClickListener){
        this.listener = letterClickListener;
    }

    private onLetterClickListener listener;

    interface onLetterClickListener{
        void onTouch(String letter, boolean isTouch);

    }
}
