package com.bb.bts.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CustomProgressBar extends View {
    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private Paint progressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint inactivePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint dialogPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Path dialogPath = new Path();

    private ValueAnimator.AnimatorUpdateListener updateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            progress = (Integer) valueAnimator.getAnimatedValue();
            invalidate();
        }
    };

    private int animDuration = 500;

    public void setProgress(int progress,boolean withAnimation){
        if (progress == this.progress)
            return;
        if (!withAnimation) {
            this.progress = progress;
            invalidate();
        } else {
            ValueAnimator increaseAnimator = ValueAnimator.ofInt(this.progress,progress);
            increaseAnimator.setDuration(animDuration);
            increaseAnimator.addUpdateListener(updateListener);
            increaseAnimator.start();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int desiredWidth = 100;
        int desiredHeight = 100;

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
                width = Math.min(desiredWidth, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
                width = desiredWidth;
        }

        int height = 0;
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(heightSize, desiredHeight);
                break;
            case MeasureSpec.UNSPECIFIED:
                height = desiredHeight;
        }
        setMeasuredDimension(width, height);
    }

    private int barLength;
    private int startBarX;
    private int endBarX;
    private int cX;
    private int cY;
    private float strokeWidth;
    private float dialogWidth;
    private float dialogHeight;

    private int progress =0;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        barLength = w;
        startBarX = barLength / 20;
        endBarX = barLength - barLength / 20;
        strokeWidth = barLength / 30f;
        cX = barLength / 2;
        cY = (int) (h - strokeWidth);
        dialogHeight = barLength / 30f;
        dialogWidth = barLength / 18f;

        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setShader(new LinearGradient(startBarX, 0, endBarX, 0,
                Color.parseColor("#F260E2"),
                Color.parseColor("#E6168E"),
                Shader.TileMode.MIRROR));
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(strokeWidth);

        inactivePaint.setStyle(Paint.Style.STROKE);
        inactivePaint.setColor(Color.parseColor("#121115"));
        inactivePaint.setStrokeCap(Paint.Cap.ROUND);
        inactivePaint.setStrokeWidth(strokeWidth);

        dialogPaint.setStyle(Paint.Style.FILL);
        dialogPaint.setColor(Color.parseColor("#F260E2"));

        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(barLength / 45f);
        textPaint.setTextAlign(Paint.Align.LEFT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float currentX = getEndByProgress(progress);
        canvas.drawLine(startBarX, cY, endBarX, cY, inactivePaint);
        canvas.drawLine(startBarX, cY, currentX, cY, progressPaint);
        canvas.save();
        canvas.translate(barLength/110f,0f);
        canvas.drawPath(createDialogPath(currentX), dialogPaint);
        String text;
        if (progress > 100)
            text = "100%";
        else
            text = progress +"%";
        float textWidth = textPaint.measureText(text);
        canvas.drawText(text,
                    currentX - textWidth/2,
                    cY - strokeWidth / 2 - barLength / 120f - dialogHeight / 2,
                    textPaint);
        canvas.restore();
    }


    private Path createDialogPath(float currentX) {
        dialogPath.reset();
        dialogPath.moveTo(currentX, cY - strokeWidth / 2);
        dialogPath.lineTo(currentX - barLength / 90f, cY - strokeWidth / 2 - barLength / 70f);
        dialogPath.lineTo(currentX + barLength / 90f, cY - strokeWidth / 2 - barLength / 70f);
        dialogPath.lineTo(currentX, cY - strokeWidth / 2);
        dialogPath.addRoundRect(
                currentX - dialogWidth / 2,
                cY - strokeWidth / 2 - barLength / 70f - dialogHeight,
                currentX + dialogWidth / 2,
                cY - strokeWidth / 2 - barLength / 70f,
                10f, 10f, Path.Direction.CW);
        return dialogPath;
    }

    private float getEndByProgress(int progress) {
        if (progress > 100)
            return endBarX;
        return startBarX + ((endBarX - startBarX) * progress / 100f);
    }
}
