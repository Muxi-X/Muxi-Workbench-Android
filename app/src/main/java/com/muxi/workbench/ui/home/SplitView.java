package com.muxi.workbench.ui.home;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.muxi.workbench.R;

public class SplitView extends View {
    TextPaint mTPDate = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    TextPaint mTPSign = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    Paint mPCircle = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPLine = new Paint(Paint.ANTI_ALIAS_FLAG);
    TextView textView;
    float mDateSize, mSignSize, mCircleRadius;
    String mDateColor, mSignColor;
    String mTextDate = "", mTextSign = "";

    public SplitView(Context context) {
        super(context);
        init();
    }

    public SplitView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SplitView);

        mTextDate = mTypedArray.getString(R.styleable.SplitView_date_text);
        mTextSign = mTypedArray.getString(R.styleable.SplitView_sign_text);
        mDateColor = mTypedArray.getString(R.styleable.SplitView_data_color);
        mSignColor = mTypedArray.getString(R.styleable.SplitView_sign_color);
        mDateSize = mTypedArray.getDimensionPixelSize(R.styleable.SplitView_date_size, 40);
        mSignSize = mTypedArray.getDimensionPixelSize(R.styleable.SplitView_sign_size, 40);
        mCircleRadius = mTypedArray.getFloat(R.styleable.SplitView_circle_radius, 100);

        mTypedArray.recycle();
        init();

    }

    public SplitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    private void init() {
        mPCircle.setColor(getResources().getColor(R.color.colorPrimary));
        mPCircle.setStrokeWidth(20);
        mPCircle.setStyle(Paint.Style.STROKE);

        mPLine.setColor(Color.GRAY);

        mTPDate.setColor(getResources().getColor(R.color.colorAccent));
        mTPDate.setTextSize(mDateSize);

        mTPSign.setColor(Color.GRAY);
        mTPSign.setTextSize(mSignSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        int radius = (int) mCircleRadius;

        //圆心
        int cx = width / 24 + radius;
        int cy = height / 2;

        int mLineLength = (int) (width * 0.7);


        //文字的宽度
        int mDTWidth = (int) mTPDate.measureText(mTextDate);
        int mTimeWidth = (int) mTPDate.measureText(mTextSign);

        //文字的y轴
        Paint.FontMetrics fontMetrics = mTPDate.getFontMetrics();
        int y = (int) (height / 2 + (Math.abs(fontMetrics.ascent) - fontMetrics.descent) / 2);


        canvas.drawCircle(cx, cy, radius, mPCircle);
        canvas.drawLine(cx + radius, cy, mLineLength + cx, cy, mPLine);

        canvas.drawText(mTextDate, cx - mDTWidth / 2, y, mTPDate);
        canvas.drawText(mTextSign, width - mTimeWidth, y, mTPSign);


    }


    public float getDateSize() {
        return mDateSize;
    }

    public float getSignSize() {
        return mSignSize;
    }

    public float getCircleRadius() {
        return mCircleRadius;
    }

    public String getDateColor() {
        return mDateColor;
    }

    public String getSignColor() {
        return mSignColor;
    }

    public String getTextDate() {
        return mTextDate;
    }

    public String getTextSign() {
        return mTextSign;
    }

    public void setDateSize(float mDateSize) {
        this.mDateSize = mDateSize;
    }

    public void setSignSize(float mTimeSize) {
        this.mSignSize = mTimeSize;
    }

    public void setCircleRadius(float mCircleRadius) {
        this.mCircleRadius = mCircleRadius;
    }

    public void setDateColor(String mDateColor) {
        this.mDateColor = mDateColor;
    }

    public void setSignColor(String mTimeColor) {
        this.mSignColor = mTimeColor;
    }

    public void setTextDate(String mTextDate) {
        this.mTextDate = mTextDate;
    }

    public void setTextSign(String mTextTime) {
        this.mTextSign = mTextTime;
    }


}
