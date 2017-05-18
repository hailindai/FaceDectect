package com.dreamguard.facedectectdemo;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.dreamguard.facedetect.FaceTracker;
import com.dreamguard.facedetect.FaceView;
import com.dreamguard.facedetect.IFaceTrackerCallback;
import com.dreamguard.facedetect.ImageProc;


public class MainActivity extends AppCompatActivity{

    private final static String TAG = "MainActivity";

    protected FaceView mFaceView;

    protected FaceTracker mFaceTracker;

    protected int orientation = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG,"onCreate");

        mFaceView = (FaceView)findViewById(R.id.faceView);

        Configuration configuration = getResources().getConfiguration();
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            orientation = 1;
        }

        mFaceTracker = new FaceTracker();

        mFaceTracker.init(this, new IFaceTrackerCallback() {
            @Override
            public void onImage(byte[] image) {
                mFaceView.setFaceBitmap(ImageProc.getBitmap(image,orientation));
            }


            @Override
            public void onData(int[] data) {
                mFaceView.setPoints(data);
                mFaceView.invalidate();
            }
        },orientation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFaceTracker.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mFaceTracker.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFaceTracker.destroy();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d(TAG,"onDestroy");
    }
}
