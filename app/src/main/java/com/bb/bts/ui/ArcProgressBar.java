package com.bb.bts.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class ArcProgressBar extends View {

    interface OnFinishListener{
        void onFinish();
    }

    public ArcProgressBar(Context context) {
        super(context);
    }

    public ArcProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ArcProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public ArcProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Paint arcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint inactiveArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint cursorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint duffPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path cursorPath = new Path();

    private Bitmap bitmap;
    private Canvas bitmapCanvas;

    private int inactiveColor = Color.parseColor("#171224");

    private float cX;
    private float cY;
    private float r;
    private float angle = 0f;

    private ValueAnimator increaseAnimator = ValueAnimator.ofInt(0,180);
    private ValueAnimator decreaseAnimator = ValueAnimator.ofInt(180,0);

    private OnFinishListener listener;
    private long duration = 2000L;

    public void setAnimationDuration(long duration){
        this.duration = duration;
        increaseAnimator.setDuration(this.duration);
    }

    public void setProgress(int progress){
        angle = 180f * progress/100;
        invalidate();
    }

    public void startIncreaseAnimation(){
        increaseAnimator.start();
    }

    public void startDecreaseAnimation(){
        decreaseAnimator.start();
    }

    public void setOnFinish(OnFinishListener listener){
        this.listener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(heightSize, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = Math.min(heightSize, widthSize);
                ;
        }

        int height = 0;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = Math.min(heightSize, widthSize);
                ;
        }
        setMeasuredDimension(width, height);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        cX = w / 2f;
        cY = h * 0.8f;
        if (w < h) {
            r = w / 2.2f;
        } else {
            r = h / 2.2f;
        }

        arcPaint.setStyle(Paint.Style.FILL);
        arcPaint.setShader(new LinearGradient(0, 0, getWidth(), 0,
                Color.parseColor("#F260E2"),
                Color.parseColor("#E6168E"),
                Shader.TileMode.MIRROR));

        inactiveArcPaint.setStyle(Paint.Style.FILL);
        inactiveArcPaint.setColor(inactiveColor);

        duffPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        duffPaint.setStyle(Paint.Style.FILL);
        duffPaint.setColor(Color.TRANSPARENT);

        float cursorH = r / 3;
        float cursorR = r / 8;

        cursorPaint.setStyle(Paint.Style.FILL);
        if (isSelected()) {
            cursorPaint.setShader(new LinearGradient(0, cY - cursorH, 0, cY + cursorR,
                    Color.parseColor("#F260E2"),
                    Color.parseColor("#E6168E"),
                    Shader.TileMode.MIRROR));
        } else {
            cursorPaint.setColor(inactiveColor);
        }

        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setColor(Color.WHITE);

        cursorPath.moveTo(cX - cursorR, cY);
        cursorPath.lineTo(cX, cY - cursorH);
        cursorPath.lineTo(cX + cursorR, cY);
        cursorPath.arcTo(cX - cursorR, cY - cursorR, cX + cursorR, cY + cursorR, 0f, 180f, false);

        bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);


        ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                angle = (Integer) valueAnimator.getAnimatedValue();
                invalidate();
            }
        };

        AnimatorListenerAdapter animatorListener = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                listener.onFinish();
            }
        };

        increaseAnimator.setDuration(duration);
        decreaseAnimator.setDuration(duration);

        increaseAnimator.addUpdateListener(updateListener);
        decreaseAnimator.addUpdateListener(updateListener);

        increaseAnimator.addListener(animatorListener);
        decreaseAnimator.addListener(animatorListener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        bitmapCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        bitmapCanvas.drawArc(cX - r, cY - r, cX + r, cY + r, 180f, 180f, true, inactiveArcPaint);
        if (isSelected())
            bitmapCanvas.drawArc(cX - r, cY - r, cX + r, cY + r, 180f, angle, true, arcPaint);
        bitmapCanvas.drawCircle(cX, cY, r / 2, duffPaint);

        canvas.drawBitmap(bitmap, 0f, 0f, arcPaint);
        drawPoints(canvas);

        canvas.save();
        canvas.rotate(angle - 90f, cX, cY);
        canvas.drawPath(cursorPath, cursorPaint);
        canvas.restore();
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (!selected) {
            cursorPaint.setColor(inactiveColor);
        } else {
            float cursorH = r / 3;
            float cursorR = r / 8;

            cursorPaint.setShader(new LinearGradient(0, cY - cursorH, 0, cY + cursorR,
                    Color.parseColor("#F260E2"),
                    Color.parseColor("#E6168E"),
                    Shader.TileMode.MIRROR));
        }
    }

    private void drawPoints(Canvas canvas){
        for (int i = 0; i < 7; i++){
            float[] coordinates = getCoordinatesOnCircle(-30*i,r);
            canvas.drawCircle(coordinates[0],coordinates[1],r/20,pointPaint);
        }
    }

    private float[] getCoordinatesOnCircle(double angle, float radius) {
        float x = (float) (radius * Math.cos(angle*0.0174533) + cX);
        float y = (float) (radius * Math.sin(angle*0.0174533) + cY);
        return new float[] { x, y };
    }

}
