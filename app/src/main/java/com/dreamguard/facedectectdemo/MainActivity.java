package com.dreamguard.facedectectdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.dreamguard.facedetect.FaceTracker;
import com.dreamguard.facedetect.FaceView;
import com.dreamguard.facedetect.IFaceTrackerCallback;
import com.dreamguard.facedetect.ImageProc;


public class MainActivity extends AppCompatActivity{

    protected FaceView mFaceView;

    protected FaceTracker mFaceTracker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFaceView = (FaceView)findViewById(R.id.faceView);
        mFaceTracker = new FaceTracker();
        mFaceTracker.init(this, new IFaceTrackerCallback() {
            @Override
            public void onImage(byte[] image) {
                mFaceView.setFaceBitmap(ImageProc.getBitmap(image));
            }

            @Override
            public void onData(int[] data) {
                mFaceView.setPoints(data);
                mFaceView.invalidate();
            }
        });
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
    }
}
