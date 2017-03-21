package com.dreamguard.facedetect;

/**
 * Created by hailin.dai on 3/16/17.
 * email:hailin.dai@wz-tech.com
 */

public class FaceDetector {

    static {
        System.loadLibrary("facedetector");
    }
    public native static void init(String path);

    public native static void readValue(byte[] frame,int[] points,int orientation);

    public native static void destroy();

}
