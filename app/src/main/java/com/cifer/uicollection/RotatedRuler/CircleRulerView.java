package com.cifer.uicollection.RotatedRuler;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.List;

/**
 * Created by cifer
 * on 5/17/18.
 */

public class CircleRulerView extends View{

    private Paint mPaint;
    private Context mContext;
    private final int Radius = 360;
    private final int mdotpointX = 635;
    private final int mdotpointY = 360;
    //
    private int mleftdotpointX = mdotpointX - Radius;
    private int mleftdotpointY = mdotpointY;
    //
    private int mrightdotpointX = mdotpointX + Radius;
    private int mrightdotpointY = mdotpointY;

    float pointX;
    float pointY;

    int rawX;
    int rawY;
    private Point mLeftPoint ;
    private Point mRightPoint;

    private final int wavenum = 80;
    private RulerCaculator rulerCalculator;
    private Paint rulerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final int toucharea = 80;

    private int mPathSpace = 10;
    public CircleRulerView(Context context){
        this(context,null);
    }

    public CircleRulerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        initRuleView();

    }


    private void initRuleView(){
        mLeftPoint = new Point(mleftdotpointX,mleftdotpointY);
        mRightPoint = new Point(mrightdotpointX,mrightdotpointY);
        Log.i("rule_view","getWidth() : " + getWidth() + "getHeight() : " + getHeight());
        rulerCalculator = new RulerCaculator(getWidth(), getHeight(),
                25, 45, 25);
        rulerPaint.setColor(Color.BLACK);
        rulerPaint.setStrokeWidth(3);
        rulerPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int center = 300;
        int rightwidth = dp2px(mContext,3);

        mPaint.setARGB(155,167,190,206);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mdotpointX,mdotpointY,Radius,mPaint);

        mPaint.setARGB(255,212,225,223);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mdotpointX,mdotpointY,Radius+1+rightwidth/2,mPaint);
        super.onDraw(canvas);

        onDrawLeftdot(canvas);
        onDrawRightdot(canvas);
        //onDrawline(canvas);
        drawRuler(canvas);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int pointcount = event.getPointerCount();

        if(pointcount >=2){
            return false;
        }

        int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                pointX = event.getRawX();
                pointY = event.getRawY();

                rawX = (int) event.getRawX();
                rawY = (int) event.getRawY();

/*                Log.d("xiao111","pointX = " + pointX);
                Log.d("xiao111","pointY = " + pointY);
                if((pointX-mdotpointX)*(pointX-mdotpointX) + (pointY- mdotpointY)*(pointY- mdotpointY) <= (Radius+20)*(Radius+20)
                        && (pointX-mdotpointX)*(pointX-mdotpointX) + (pointY- mdotpointY)*(pointY- mdotpointY) >= (Radius-20)*(Radius-20)){
                    if(pointX < mdotpointX){
                        mleftdotpointY = (int)pointY;
                        mleftdotpointX = -(int) Math.sqrt(Radius*Radius - (mleftdotpointY - mdotpointY) * (mleftdotpointY - mdotpointY)) + mdotpointX;
                    }else {
                        mrightdotpointY= (int)pointY;
                        mrightdotpointX =(int) Math.sqrt(Radius*Radius - (mrightdotpointY - mdotpointY) * (mrightdotpointY - mdotpointY)) + mdotpointX;
                    }
                    Log.d("xiao111","ACTION_DOWN = " + mleftdotpointX + "," + mleftdotpointY);
                    Log.d("xiao111","ACTION_DOWN = " + mrightdotpointX + "," + mrightdotpointY);
                    postInvalidate();
                }*/

                break;
            case MotionEvent.ACTION_MOVE:
                pointX = event.getRawX();
                pointY = event.getRawY();

                if((pointX-mdotpointX)*(pointX-mdotpointX) + (pointY- mdotpointY)*(pointY- mdotpointY) <= (Radius+wavenum)*(Radius+wavenum)
                        && (pointX-mdotpointX)*(pointX-mdotpointX) + (pointY- mdotpointY)*(pointY- mdotpointY) >= (Radius-wavenum)*(Radius-wavenum)){
                    if(pointX < mdotpointX){
                        mleftdotpointY = (int)pointY;
                        mleftdotpointX = -(int) Math.sqrt(Radius*Radius - (mleftdotpointY - mdotpointY) * (mleftdotpointY - mdotpointY)) + mdotpointX;
                    }else {
                        mrightdotpointY= (int)pointY;
                        mrightdotpointX =(int) Math.sqrt(Radius*Radius - (mrightdotpointY - mdotpointY) * (mrightdotpointY - mdotpointY)) + mdotpointX;
                    }

                }else if((pointX-mdotpointX)*(pointX-mdotpointX) + (pointY- mdotpointY)*(pointY- mdotpointY) <= (Radius-20)*(Radius-20)){
                    int yX = (int) event.getRawX();
                    int yY = (int) event.getRawY();

                    int jY=yY-rawY;

                    mleftdotpointY = mleftdotpointY + jY;
                    mrightdotpointY = mrightdotpointY+ jY;
                    mleftdotpointX = -(int) Math.sqrt(Radius*Radius - (mleftdotpointY - mdotpointY) * (mleftdotpointY - mdotpointY)) + mdotpointX;
                    mrightdotpointX =(int) Math.sqrt(Radius*Radius - (mrightdotpointY - mdotpointY) * (mrightdotpointY - mdotpointY)) + mdotpointX;

                    if(mleftdotpointY > 720){
                        mleftdotpointY = 720;
                        mleftdotpointX = -(int) Math.sqrt(Radius*Radius - (mleftdotpointY - mdotpointY) * (mleftdotpointY - mdotpointY)) + mdotpointX;
                    }
                    if(mleftdotpointY < 10){
                        mleftdotpointY = 10;
                        mleftdotpointX = -(int) Math.sqrt(Radius*Radius - (mleftdotpointY - mdotpointY) * (mleftdotpointY - mdotpointY)) + mdotpointX;

                    }

                    if(mrightdotpointY > 720){
                        mrightdotpointY = 720;
                        mrightdotpointX =(int) Math.sqrt(Radius*Radius - (mrightdotpointY - mdotpointY) * (mrightdotpointY - mdotpointY)) + mdotpointX;


                    }
                    if(mrightdotpointY < 10){
                        mrightdotpointY = 10;
                        mrightdotpointX =(int) Math.sqrt(Radius*Radius - (mrightdotpointY - mdotpointY) * (mrightdotpointY - mdotpointY)) + mdotpointX;
                    }


                    pointY = yY;
                    pointX = yX;
                    rawY = yY;
                    rawX = yX;
                }
                invalidate();
                mLeftPoint.x = mleftdotpointX;
                mLeftPoint.y = mleftdotpointY;
                mRightPoint.x = mrightdotpointX;
                mRightPoint.y = mrightdotpointY;


                Log.d("xiao111","mlistener :" + mlistener);
                if(mlistener != null){
                    mlistener.getLeftposition(mleftdotpointX,mleftdotpointY);
                    mlistener.getRightposition(mrightdotpointX,mrightdotpointY);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        getParent().requestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(ev);
    }

    private int dp2px(Context context, float width){
        final float scale = context.getResources().getDisplayMetrics().density;
        return  (int)(width * scale + 0.5f);
    }

    private void onDrawLeftdot(Canvas mcanvas ){
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);
        mcanvas.drawCircle(mleftdotpointX,mleftdotpointY,10,mPaint);
        //mlistener.getLeftposition(mleftdotpointX,mleftdotpointY);
    }

    private void onDrawRightdot(Canvas mcanvas){
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.FILL);
        mcanvas.drawCircle(mrightdotpointX,mrightdotpointY,10,mPaint);
        //mlistener.getRightposition(mrightdotpointX,mrightdotpointY);
    }

    private void onDrawline(Canvas canvas){
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(2);
        canvas.drawLine(mleftdotpointX,mleftdotpointY,mrightdotpointX,mrightdotpointY,mPaint);

    }

    private OnPositionTakeListener mlistener;
    public void setOnPositionTakeListener(OnPositionTakeListener listener){
        Log.d("xiao111","listener :" + listener);
        mlistener = listener;
        Log.d("xiao111","mlistener111 :" + listener);

    }

    public interface OnPositionTakeListener{
        void getLeftposition(int leftx,int lefty);
        void getRightposition(int rightx,int righty);
    }

    public boolean isOnRuler(float pointX,float pointY){
        invalidate();

        Log.d("xiao_frame","point == " + pointX + "," + pointY);

        float k = 0;
        float b = 0;


        //直线方程y = kx + b ，过点（mleftdotpointX,mleftdotpointY），（mrightdotpointX,mrightdotpointY）

        if(mleftdotpointX != mrightdotpointX){
            k = (mleftdotpointY - mrightdotpointY)/(mleftdotpointX - mrightdotpointX);
            b = mleftdotpointY - k*mleftdotpointX;
        }

        float y = k*pointX + b;
        float minusy = Math.abs(y - pointY);

        Log.d("xiao_frame","y      == " + y);
        Log.d("xiao_frame","minusy == " + minusy);

        if(minusy > toucharea ){
            return false;
        }

        return true;
    }

    private void drawRuler(Canvas canvas) {
        if (rulerCalculator != null) {
            rulerCalculator.calculate(
                    RulerCaculator.point2PointO(mRightPoint),
                    RulerCaculator.point2PointO(mLeftPoint));
            // 画尺子边框
            List<Point> rulerPoints = RulerCaculator
                    .pointfs2Points(rulerCalculator.getRulerPoints());
            Log.i("rule_view","rulerPoints : " + rulerPoints);
            Log.i("rule_view","rulerPoints.size() : " + rulerPoints.size());
            if (rulerPoints != null && rulerPoints.size() == 4) {
                // 画线顺序：上下左右
                Path pathRuler2 = new Path();
                pathRuler2.moveTo(rulerPoints.get(1).x, rulerPoints.get(1).y-mPathSpace);
                pathRuler2.lineTo(rulerPoints.get(3).x, rulerPoints.get(3).y-mPathSpace);
                canvas.drawPath(pathRuler2, rulerPaint);
            }
            // 画尺子刻度
            List<Point> bottomScalePoints = RulerCaculator
                    .pointfs2Points(rulerCalculator.getRulerBottomScalePoints());
            List<Point> top1ScalePoints = RulerCaculator
                    .pointfs2Points(rulerCalculator.getRulerTop1ScalePoints());
            List<Point> top2ScalePoints = RulerCaculator
                    .pointfs2Points(rulerCalculator.getRulerTop2ScalePoints());
            Log.i("rule_view","bottomScalePoints : " + bottomScalePoints);
            Log.i("rule_view","top1ScalePoints : " + top1ScalePoints);
            Log.i("rule_view","top2ScalePoints : " + top2ScalePoints);
            // 画尺子小刻度
            if (bottomScalePoints != null && bottomScalePoints.size() > 0
                    && top1ScalePoints != null && top1ScalePoints.size() > 0) {
                for (int i = 0; i < bottomScalePoints.size(); i++) {
                    try {
                        Path path = new Path();
                        path.moveTo(top1ScalePoints.get(i).x,
                                top1ScalePoints.get(i).y-mPathSpace);
                        path.lineTo(bottomScalePoints.get(i).x,
                                bottomScalePoints.get(i).y-mPathSpace);
                        canvas.drawPath(path, rulerPaint);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.i("rule_view","bottomScalePoints222222222222 : " + bottomScalePoints.size());
            Log.i("rule_view","top2ScalePoints222222222222 : " + top2ScalePoints.size());
            // 画尺子大刻度
            if (bottomScalePoints != null && bottomScalePoints.size() > 0
                    && top2ScalePoints != null && top2ScalePoints.size() > 0) {
                for (int i = 0; i < top2ScalePoints.size(); i++) {
                    int index = (i + 1) * 5 - 1;
                    if (bottomScalePoints.size() > index) {
                        Path path = new Path();
                        path.moveTo(top2ScalePoints.get(i).x,
                                top2ScalePoints.get(i).y-mPathSpace);
                        path.lineTo(bottomScalePoints.get((i + 1) * 5 - 1).x,
                                bottomScalePoints.get((i + 1) * 5 - 1).y-mPathSpace);
                        canvas.drawPath(path, rulerPaint);
                    }
                }
            }
        }
    }

}
