package com.example.android.sunandmoon;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;

public class ClockActivitySet extends View {

    public static boolean is24;
    public static boolean hourOnTop;
    private final ArrayList<DialOverlaySet> mDialOverlay = new ArrayList<DialOverlaySet>();
    AttributeSet attributeSet;
    int defStyle;
    private Calendar mCalendar;
    private Drawable mFace;
    private int mDialWidth;
    private int mDialHeight;
    private int mBottom;
    private int mTop;
    private int mLeft;
    private int mRight;
    private boolean mSizeChanged;
    private HandsOverlaySet mHandsOverlay;
    private boolean autoUpdate;

    public static int hours;
    public static int minutes;
    public static int seconds;
    int nothing = 0;

    private static final String TAG = "SoLunar";

    public ClockActivitySet(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, R.drawable.default_face1, R.drawable.default_hour1, R.drawable.default_minute, 0, false, false);
    }

    public ClockActivitySet(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, R.drawable.default_face1, R.drawable.default_hour1, R.drawable.default_minute, 0, false, false);
    }

    public ClockActivitySet(Context context) {
        super(context);
        init(context, R.drawable.default_face1, R.drawable.default_hour1, R.drawable.default_minute, 0, false, false);
    }


    public void setFace(int drawableRes) {
        final Resources r = getResources();
        setFace(r.getDrawable(drawableRes));
    }

    public void init(Context context, @DrawableRes int watchFace, @DrawableRes int hourHand, @DrawableRes int minuteHand, int alpha, boolean is24, boolean hourOnTop) {
        ClockActivitySet.is24 = is24;
        final TypedArray attrs = context.obtainStyledAttributes(attributeSet, R.styleable.CustomAnalogClock, defStyle, 0);
        Drawable face = attrs.getDrawable(R.styleable.CustomAnalogClock_face);
        Drawable Hhand = attrs.getDrawable(R.styleable.CustomAnalogClock_hour_hand);
        Drawable Mhand = attrs.getDrawable(R.styleable.CustomAnalogClock_minute_hand);

        ClockActivitySet.hourOnTop = hourOnTop;
        setFace(watchFace);
        Hhand = context.getResources().getDrawable(hourHand);
        assert Hhand != null;
        if (alpha > 0)
            Hhand.setAlpha(alpha);

        Mhand = context.getResources().getDrawable(minuteHand);

        mCalendar = Calendar.getInstance();

        mHandsOverlay = new HandsOverlaySet(Hhand, Mhand);
    }

    public void setFace(Drawable face) {
        mFace = face;
        mSizeChanged = true;
        mDialHeight = mFace.getIntrinsicHeight();
        mDialWidth = mFace.getIntrinsicWidth();

        invalidate();
    }

    /**
     * Sets the currently displayed time in {@link System#currentTimeMillis()}
     * time.
     *
     *  the time to display on the clock
     */

    public void setTime(int hrs,int min, int sec) {



        int hour = hrs;
        int minute = min;
        int second = sec;

        //mCalendar.setTimeInMillis(time);

        invalidate();

    }




    /**
     * Sets the currently displayed time.
     *
     * @param calendar The time to display on the clock
     */

    public void setTime(Calendar calendar) {

        mCalendar = calendar;
        invalidate();
        if (autoUpdate) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    setTime(hours,minutes,seconds);
                }
            }, 5000);
        }
    }

    public void setAutoUpdate(boolean autoUpdate) {
        this.autoUpdate = autoUpdate;
        setTime(hours,minutes,seconds);
    }


    /**
     * Sets the timezone to use when displaying the time.
     *
     *
     */
    //public void setTimezone(TimeZone timezone) {
    // mCalendar = Calendar.getInstance(timezone);
    // }

    public void setHandsOverlay(HandsOverlaySet handsOverlay) {
        mHandsOverlay = handsOverlay;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mSizeChanged = true;
    }

    // some parts from AnalogClock.java
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final boolean sizeChanged = mSizeChanged;
        mSizeChanged = false;

        final int availW = mRight - mLeft;
        final int availH = mBottom - mTop;

        final int cX = availW / 2;
        final int cY = availH / 2;

        final int w = mDialWidth;
        final int h = mDialHeight;

        boolean scaled = false;

        if (availW < w || availH < h) {
            scaled = true;
            final float scale = Math.min((float) availW / (float) w,
                    (float) availH / (float) h);
            canvas.save();
            canvas.scale(scale, scale, cX, cY);
        }

        if (sizeChanged) {
            mFace.setBounds(cX - (w / 2), cY - (h / 2), cX + (w / 2), cY
                    + (h / 2));
        }

        mFace.draw(canvas);

        for (final DialOverlaySet overlay : mDialOverlay) {
            overlay.onDraw(canvas, cX, cY, w, h, hours,minutes,seconds, sizeChanged);
        }

        mHandsOverlay.onDraw(canvas, cX, cY, w, h, hours,minutes,seconds, sizeChanged);

        if (scaled) {
            canvas.restore();
        }
    }

    // from AnalogClock.java
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        float hScale = 1.0f;
        float vScale = 1.0f;

        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {
            hScale = (float) widthSize / (float) mDialWidth;
        }

        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {
            vScale = (float) heightSize / (float) mDialHeight;
        }

        final float scale = Math.min(hScale, vScale);

        setMeasuredDimension(
                getDefaultSize((int) (mDialWidth * scale), widthMeasureSpec),
                getDefaultSize((int) (mDialHeight * scale), heightMeasureSpec));
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return mDialHeight;
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return mDialWidth;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        // because we don't have access to the actual protected fields
        mRight = right;
        mLeft = left;
        mTop = top;
        mBottom = bottom;
    }


    public void addDialOverlay(DialOverlaySet dialOverlay) {
        mDialOverlay.add(dialOverlay);
    }

    public void removeDialOverlay(DialOverlaySet dialOverlay) {
        mDialOverlay.remove(dialOverlay);
    }

    public void clearDialOverlays() {
        mDialOverlay.clear();
    }


}
