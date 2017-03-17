package com.dreamguard.facedetect;

/**
 * Created by hailin.dai on 3/17/17.
 * email:hailin.dai@wz-tech.com
 */

public interface IFaceTrackerCallback {

    public void onImage(byte[] image);

    public void onData(int[] data);

}
