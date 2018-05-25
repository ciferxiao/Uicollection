package com.cifer.uicollection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private Letterlist letterlist;
    private TextView textView;
    private ImageButton imageButton;

    private RotateSensorUtil sensorUtil;
    private ArrayList<View> rotateViews = new ArrayList<>();

    private FontSizeSeekbar sizeSeekbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.text);
        letterlist = (Letterlist)findViewById(R.id.letterlist);


        imageButton = (ImageButton)findViewById(R.id.imageview);
        rotateViews.add(imageButton);
        sensorUtil = new RotateSensorUtil(this,rotateViews);

        letterlist.setonLetterTouchListener(new Letterlist.onLetterClickListener() {
            @Override
            public void onTouch(String letter, boolean isTouch) {
                if(isTouch){
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(letter);
                }else {
                    textView.setVisibility(View.INVISIBLE);
                }
            }
        });
        //seekbar 回调
        sizeSeekbar.setResponseOnTouch(new FontSizeSeekbar.ResponseOnTouch() {
            @Override
            public void onTouchResponse(int section) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorUtil.unregisterSensor();
    }

}
