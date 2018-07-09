package com.cifer.uicollection;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cifer.uicollection.PasswordUi.CustomerKeyboard;
import com.cifer.uicollection.PasswordUi.PassWordEditText;

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

    //passwordUi 调用方法
    private void passwordshow(){
        LayoutInflater inflater = LayoutInflater.from(this);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pw_layout, null);

        final Dialog dialog = new Dialog(this);
        dialog.setContentView(layout);
        Window window = dialog.getWindow();
        assert window != null;
        window.setWindowAnimations(R.style.dial_style);
        window.setGravity(Gravity.BOTTOM);
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height =WindowManager.LayoutParams.WRAP_CONTENT;
        WindowManager.LayoutParams lp = window.getAttributes(); // 获取对话框当前的参数值
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        dialog.show();
        CustomerKeyboard customerKeyboard = (CustomerKeyboard) layout.findViewById(R.id.keyboard_View);
        final PassWordEditText pw_EditText = (PassWordEditText)layout. findViewById(R.id.pw_EditText);
        customerKeyboard.setOnCustomerKeyboardClickListener(new CustomerKeyboard.CustomerKeyboardClickListener() {
            @Override
            public void click(String pw) {
                pw_EditText.addPW(pw);
            }

            @Override
            public void remove() {
                pw_EditText.removePW();
            }

            @Override
            public void dismiss() {
                dialog.dismiss();
            }
        });

        pw_EditText.setOnPWCommitListener(new PassWordEditText.PWCommitListener() {
            @Override
            public void commit_PW(String pw) {
                if(pw.equals("1234")){
                    Intent intent = new Intent();
                    startActivity(intent);
                    dialog.dismiss();
                }else if(pw.length()>3 && !pw.equals("1234")){
                    pw_EditText.clear();
                }
            }
        });

    }




}
