package com.example.android.sunandmoon;

import android.graphics.Canvas;

/**
 * Created by Karthik on 10/11/2016.
 */

public interface DialOverlayRise {

    public abstract void onDraw(Canvas canvas, int cX, int cY, int w, int h, int hours, int minutes, int seconds,
                                boolean sizeChanged);
}
