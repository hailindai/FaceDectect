package com.dreamguard.facedetect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by hailin.dai on 3/16/17.
 * email:hailin.dai@wz-tech.com
 */

public class FaceView extends View {
    protected Bitmap faceBitmap;

    protected int points[];

    public int[] getPoints() {
        return points;
    }

    public void setPoints(int[] points) {
        this.points = points;
    }

    public FaceView(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        points = new int[132];
    }

    public Bitmap getFaceBitmap() {
        return faceBitmap;
    }

    public void setFaceBitmap(Bitmap faceBitmap) {
        this.faceBitmap = faceBitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
//		// super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        try {
            canvas.drawBitmap(faceBitmap, 0, 0, new Paint());
        }catch (Exception e){
        }
        for(int i=0;i<66;i++) {
            canvas.drawCircle(640-points[i], points[i + 66], 5, paint);
        }
    }
}
