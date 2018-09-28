package com.mvp.cifer.mvpdemo.view;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Interpolator;

import com.mvp.cifer.mvpdemo.MainActivity;
import com.mvp.cifer.mvpdemo.R;

 
public class CircleCalculator extends View {

    //View默认最小宽度
    private static final int DEFAULT_MIN_WIDTH = 400;
    private static final String TAG = "CircularProgressView";
    public static int animatorDuration = 2000;         //动画执行时间
    float x0 = 0;
    float y0 = 0;
    float r = 0;
    //绿色圆环
    private int[] greens = new int[]{getResources().getColor(R.color.colorAccent), getResources().getColor(R.color.colorPrimary)};
    private int width;
    private int height;
    private float currentValueInterest = 0f;             //百分百
    private Paint paint;
    private int TEXT_SIZE = 45;
    private String percentage = "0";
    private String text;
    private String cityName;
    /**
     * 百分百计算
     *
     * @param percentage 百分百
     * @param type  1 2 3
     */
    private float type;

    MainActivity activity;
    public CircleCalculator(Context context) {
        this(context,null);

    }

    public CircleCalculator(Context context, AttributeSet attrs) {
        this(context, attrs,0);
        init(context, attrs);
    }

    public CircleCalculator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        activity = (MainActivity)context;
        init(context, attrs);

    }

    /**
     * 获取屏幕宽、高度
     */
    public static int[] getScreenWidthAndHeight(Context context) {
        int[] int_arr = new int[2];
        Point size = new Point();
        ((MainActivity)context).getWindowManager().getDefaultDisplay().getSize(size);
        int_arr[0] = size.x;
        int_arr[1] = size.y;
        return int_arr;
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        return getScreenWidthAndHeight(context)[0];
    }

    public static int getScreenWidthPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getScreenHeightPixels(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        return getScreenWidthAndHeight(context)[1];
    }

    private void init(Context context, AttributeSet attrs) {
        paint = new Paint();
        paint.setAntiAlias(true);
        calculationTextSize(context);
    }

    /**
     * 计算贷款总额文字大小
     */
    private void calculationTextSize(Context context) {
        int screenWidth = getScreenWidth(context);
        int screenHeight = getScreenHeight(context);
        float ratioWidth = (float) screenWidth / 1080;
        float ratioHeight = (float) screenHeight / 1776;
        float RATIO = Math.min(ratioWidth, ratioHeight);
        TEXT_SIZE = Math.round(35 * RATIO);
    }

    private void resetParams() {
        width = getWidth();
        height = getHeight();
    }

    private void initPaint() {
        paint.reset();
        paint.setAntiAlias(true);
    }

    private void setValue(float value) {
        currentValueInterest = 0f;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(currentValueInterest, value);
        valueAnimator.setDuration(animatorDuration);
        valueAnimator.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float v) {
                return 1 - (1 - v) * (1 - v) * (1 - v);
            }
        });
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                currentValueInterest = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void setData(float type, int[] greens, String percentage, String cityName) {
        this.type = type;
        this.greens = greens;
        this.cityName = cityName;
        if (percentage.contains(".")) {
            percentage = percentage.substring(0, percentage.indexOf("."));
        }
        this.percentage = percentage;
        if (TextUtils.isEmpty(percentage)) {
            return;
        }
        double db_total = Double.parseDouble(percentage) / 100 * 360;
        setValue((float) db_total);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        resetParams();
        initPaint();

//设置圆环宽度
        float doughnutWidth = Math.min(width, height) / 2 * 0.03f;
        float left = (width > height ? Math.abs(width - height) / 2 : 0) + doughnutWidth * 4;
        float top = (height > width ? Math.abs(height - width) / 2 : 0) + doughnutWidth * 4;
        float right = width - (width > height ? Math.abs(width - height) / 2 : 0) - doughnutWidth * 4;
        float buttom = height - (height > width ? Math.abs(height - width) / 2 : 0) - doughnutWidth * 4;
        RectF rectF = new RectF(left, top, right, buttom);
        RectF rectF_in = new RectF(left + doughnutWidth, top + doughnutWidth, right - doughnutWidth, buttom - doughnutWidth);

        x0 = left + (right - left) / 2;
        y0 = top + (buttom - top) / 2;
        r = (right - left) / 2;

        canvas.rotate(-type, width / 2, height / 2);

        setDefault(canvas, doughnutWidth, rectF, rectF_in, Color.parseColor("#acFFFFFF"), 360, paint, Color.WHITE);

        setOtherStyle(canvas, doughnutWidth, rectF, greens);
        drawCircle(canvas, doughnutWidth);
        canvas.rotate(type, width / 2, height / 2);

        initPaint();
        paint.setColor(getResources().getColor(R.color.colorPrimaryDark));
        paint.setTextSize(TEXT_SIZE * 3);
        paint.setTextAlign(Paint.Align.CENTER);
        float one_height = (paint.getFontMetrics().descent + paint.getFontMetrics().ascent);

        float title_line = height / 2 - ((paint.getFontMetrics().descent + paint.getFontMetrics().ascent) / 2) - 4;
        canvas.drawText(percentage, width / 2, title_line, paint);
        float textLength = paint.measureText(percentage);

        paint.setTextSize(TEXT_SIZE);
        float title_line1 = title_line + one_height - 40;
        canvas.drawText("超越", width / 2, title_line1, paint);

        float title_line2 = title_line - ((paint.getFontMetrics().descent + paint.getFontMetrics().ascent) - 36);
        canvas.drawText(cityName, width / 2, title_line2, paint);

        float title_line3 = title_line + one_height - 36;
        canvas.drawText("%", width / 2 + textLength / 2 + 16, title_line, paint);

    }

    private void drawCircle(Canvas canvas, float doughnutWidth) {
        if (currentValueInterest <= 0) {
            return;
        }
        float x = (float) (x0 + (r) * Math.cos(currentValueInterest * Math.PI / 180));
        float y = (float) (y0 + (r) * Math.sin(currentValueInterest * Math.PI / 180));

        initPaint();
        paint.setColor(Color.parseColor("#50ffffff"));
        canvas.drawCircle(x, y, 30, paint);

        initPaint();
        int str_color = caculateColor(greens[0], greens[1], currentValueInterest / 360);

        paint.setColor(str_color);
        canvas.drawCircle(x, y, 16, paint);
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     *
     * @param startColor 起始颜色 int类型
     * @param endColor   结束颜色 int类型
     * @param franch     franch 百分比0.5
     * @return 返回int格式的color
     */
    public static int caculateColor(int startColor, int endColor, float franch) {
        String strStartColor = "#" + Integer.toHexString(startColor);
        String strEndColor = "#" + Integer.toHexString(endColor);
        return Color.parseColor(caculateColor(strStartColor, strEndColor, franch));
    }

    /**
     * 计算从startColor过度到endColor过程中百分比为franch时的颜色值
     *
     * @param startColor 起始颜色 （格式#FFFFFFFF）
     * @param endColor   结束颜色 （格式#FFFFFFFF）
     * @param franch     百分比0.5
     * @return 返回String格式的color（格式#FFFFFFFF）
     */
    public static String caculateColor(String startColor, String endColor, float franch) {

        int startAlpha = Integer.parseInt(startColor.substring(1, 3), 16);
        int startRed = Integer.parseInt(startColor.substring(3, 5), 16);
        int startGreen = Integer.parseInt(startColor.substring(5, 7), 16);
        int startBlue = Integer.parseInt(startColor.substring(7), 16);

        int endAlpha = Integer.parseInt(endColor.substring(1, 3), 16);
        int endRed = Integer.parseInt(endColor.substring(3, 5), 16);
        int endGreen = Integer.parseInt(endColor.substring(5, 7), 16);
        int endBlue = Integer.parseInt(endColor.substring(7), 16);

        int currentAlpha = (int) ((endAlpha - startAlpha) * franch + startAlpha);
        int currentRed = (int) ((endRed - startRed) * franch + startRed);
        int currentGreen = (int) ((endGreen - startGreen) * franch + startGreen);
        int currentBlue = (int) ((endBlue - startBlue) * franch + startBlue);

        return "#" + getHexString(currentAlpha) + getHexString(currentRed)
                + getHexString(currentGreen) + getHexString(currentBlue);

    }

    /**
     * 将10进制颜色值转换成16进制。
     */
    public static String getHexString(int value) {
        String hexString = Integer.toHexString(value);
        if (hexString.length() == 1) {
            hexString = "0" + hexString;
        }
        return hexString;
    }

    private void setDefault(Canvas canvas, float doughnutWidth, RectF rectF, RectF rectF_in, int orange, int end, Paint paint, int violet) {
        defaultStyle(canvas, doughnutWidth, rectF, orange, 360, -end, paint);
    }

    private void defaultStyle(Canvas canvas, float doughnutWidth, RectF rectF, int orange, float start, float end, Paint paint) {
        initPaint();
        paint.setStrokeWidth(doughnutWidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(orange);
        canvas.drawArc(rectF, start, end, false, paint);
    }

    private void setOtherStyle(Canvas canvas, float doughnutWidth, RectF rectF, int[] color) {
        initPaint();
        paint.setStrokeWidth(doughnutWidth);
        paint.setStyle(Paint.Style.STROKE);
        if (color.length > 1) {
            paint.setShader(new SweepGradient(width / 2, height / 2, color, null));
        } else {
            paint.setColor(color[0]);
        }
        canvas.drawArc(rectF, 0, currentValueInterest, false, paint);
    }

    /**
     * 当布局为wrap_content时设置默认长宽
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(measure(widthMeasureSpec), measure(heightMeasureSpec));
    }

    private int measure(int origin) {
        int result = DEFAULT_MIN_WIDTH;
        int specMode = MeasureSpec.getMode(origin);
        int specSize = MeasureSpec.getSize(origin);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

}

