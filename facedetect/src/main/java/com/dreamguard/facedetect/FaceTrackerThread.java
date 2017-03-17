package com.dreamguard.facedetect;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created by hailin.dai on 3/17/17.
 * email:hailin.dai@wz-tech.com
 */

public class FaceTrackerThread extends Thread {

    private Handler mHandler;

    private int[] mPoints = new int[FaceConst.POINT_SIZE];

    private FaceTrackerState mCurrentState = FaceTrackerState.IDLE;

    private Object mSync = new Object();

    public int[] getPoints() {
        return mPoints;
    }

    public Handler getHandler() {
        synchronized (mSync) {
            if (mHandler == null) {
                try {
                    mSync.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return mHandler;
    }

    public FaceTrackerState getCurrentState() {
        return mCurrentState;
    }

    @Override
    public void run() {
        super.run();

        Looper.prepare();

        synchronized (mSync) {
            mHandler = new Handler() {
                public void handleMessage(Message msg) {
                    // process incoming messages here
                    switch (msg.what){
                        case FaceConst.MSG_IMAGE:
                            mCurrentState = FaceTrackerState.RUNNING;
                            FaceDetector.readValue(msg.getData().getByteArray("imageData"),mPoints);
                            mCurrentState = FaceTrackerState.IDLE;
                            break;
                        case FaceConst.MSG_TERMINATE:
                            Looper.myLooper().quit();
                            break;
                        default:
                            super.handleMessage(msg);
                    }
                }
            };
            mSync.notify();
        }

        Looper.loop();

    }
}
