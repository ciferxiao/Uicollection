package com.cifer.uicollection;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;

import java.util.ArrayList;

/**
 * 随重力旋转图标类
 * Created by cifer on 4/12/18.
 */

public class RotateSensorUtil implements SensorEventListener{
    private Context mcontext;
    private SensorManager sensorManager;
    private ArrayList<View>  views = new ArrayList<>();
    //默认旋转角度code，分别为 0,1/2,3,4/-1
    private int curRotateCode = 0;
    private int curAngle = 0;
    public RotateSensorUtil(Context mcontext , ArrayList<View> arrayList) {
        this.mcontext = mcontext;
        this.views = arrayList;
        sensorManager = (SensorManager) mcontext.getSystemService(Context.SENSOR_SERVICE);
        /*
         SensorManager.SENSOR_DEPLAY_FASTEST   最灵敏

         SensorManager.SENSOR_DEPLAY_GAME   游戏使用

         SensorManager.SENSOR_DEPLAY_NORMAL   正常频率(较慢)

         SensorManager.SENSOR_DEPLAY_UI     最慢(横和竖)
         */
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_NORMAL);

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() != Sensor.TYPE_ACCELEROMETER){
            return;
        }

        float[] values = sensorEvent.values;
        float ax = values[0];
        float ay = values[1];
        double g = Math.sqrt(ax * ax + ay * ay);
        double cos = ay / g;
        if (cos > 1) {
            cos = 1;
        } else if (cos < -1) {
            cos = -1;
        }
        double rad = Math.acos(cos);
        if (ax < 0) {
            rad = 2 * Math.PI - rad;
        }
        int uiRot = ((Activity)mcontext).getWindowManager().getDefaultDisplay().getRotation();
        double uiRad = Math.PI / 2 * uiRot;
        rad -= uiRad;
        checkBundray((int) rad);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void checkBundray(int rotatetype){
        if(rotatetype == 2) rotatetype = 1;
        if(rotatetype == -1) rotatetype = 4;
        int angle = 0;

        switch (rotatetype){
            case 0:
                angle = 0;
                break;
            case 1:
                angle = 90;
                break;
            case 3:
                angle = 180;
                break;
            case 4:
                angle = 270;
                break;
        }

        if(rotatetype != curRotateCode){
            valueRotateAnim(angle);
            curAngle = angle;
            curRotateCode = rotatetype;
        }
    }

    private void valueRotateAnim(int angle){
        //特别处理从270-0度的反向旋转
        if(curAngle == 270){
            angle = 360;
        }
        for(int i=0;i<views.size();i++){
            startRotateAnim(views.get(i),300,curAngle,angle);
        }
    }


    private void startRotateAnim(View view,long time,int fromAngle,float toAngle){
        ObjectAnimator animRotate = ObjectAnimator.ofFloat(view,"rotation",fromAngle,toAngle);
        animRotate.setDuration(time);
        animRotate.start();
    }

    public void unregisterSensor(){
        sensorManager.unregisterListener(this);
    }

}
