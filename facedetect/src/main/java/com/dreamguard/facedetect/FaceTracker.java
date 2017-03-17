package com.dreamguard.facedetect;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.List;

import static com.dreamguard.facedetect.ImageProc.applyGrayScale;

/**
 * Created by hailin.dai on 3/17/17.
 * email:hailin.dai@wz-tech.com
 */

public class FaceTracker implements Camera.PreviewCallback{

    protected Camera mCamera;

    protected SurfaceTexture surfaceTexture = new SurfaceTexture(10);

    protected Context mContext;

    protected int mPoints[]=new int[132];

    protected IFaceTrackerCallback mCallback;

    protected FaceTrackerThread mThread;

    protected Handler mThreadHandler;

    public void saveToSDCard(Context context,String name) throws Throwable {
        InputStream inStream = context.getResources().openRawResource(R.raw.face);
        File dir = new File(Environment.getExternalStorageDirectory()+"/FaceDetector");
        if(!dir.exists()){
            dir.mkdirs();
        }
        File file = new File(Environment.getExternalStorageDirectory()+"/FaceDetector",name);
        if(!file.exists()){
            file.createNewFile();
            FileOutputStream fileOutputStream = new FileOutputStream(file);//存入SDCard
            byte[] buffer = new byte[10];
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            int len = 0;
            while((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            byte[] bs = outStream.toByteArray();
            fileOutputStream.write(bs);
            outStream.close();
            inStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
        }

    }


    public void start(){
        try {
            mCamera = Camera.open(1); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        if (mCamera != null) {

            Camera.Parameters params = mCamera.getParameters();
            params.setPreviewSize(640, 480);
            List<String> focusModes = params.getSupportedFocusModes();
            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
                params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            }
            mCamera.setParameters(params);

            try {
                mCamera.setPreviewTexture(surfaceTexture);
                mCamera.setPreviewCallback(this);
                mCamera.startPreview();

            } catch (Exception e) {
                Log.d("This", "Error starting camera preview: " + e.getMessage());
            }
        }
    }

    public void stop(){
        if (mCamera != null) {
            try {
                mCamera.stopPreview();
                mCamera.setPreviewCallback(null);
                mCamera.release();

            } catch (Exception e) {
            }
        }
    }


    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {

        if(mCallback != null){
            mCallback.onImage(bytes);
        }
        if(mCallback != null){
            mCallback.onData(mThread.getPoints());
        }
        if(mThread.getCurrentState() == FaceTrackerState.IDLE) {
            Message message = new Message();
            Bundle bundle = new Bundle();
            bundle.putByteArray("imageData", bytes);
            message.what = FaceConst.MSG_IMAGE;
            message.setData(bundle);
            mThreadHandler.sendMessage(message);
        }
    }

    public void init(Context context,IFaceTrackerCallback callback){

        mContext = context;
        mCallback = callback;
        String path = Environment.getExternalStorageDirectory()+"/FaceDetector/";

        try {
            saveToSDCard(context,"face.data");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        FaceDetector.init(path);

        mThread = new FaceTrackerThread();
        mThread.start();
        mThreadHandler = mThread.getHandler();
    }

    public void destroy(){
        mThreadHandler.getLooper().quit();
        FaceDetector.destroy();
    }
}
