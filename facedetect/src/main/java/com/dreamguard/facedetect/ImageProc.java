package com.dreamguard.facedetect;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by hailin.dai on 3/17/17.
 * email:hailin.dai@wz-tech.com
 */

public class ImageProc {

    public static int[] applyGrayScale(byte[] var0, int var1, int var2) {
        int[] var3 = new int[var2 = var1 * var2];

        for(int var4 = 0; var4 < var2; ++var4) {
            var1 = var0[var4] & 255;
            var3[var4] = -16777216 | var1 << 16 | var1 << 8 | var1;
        }

        return var3;
    }

    public static Bitmap getBitmap(byte[] bytes){
        int[] imagePixels = applyGrayScale(bytes, 640, 480);

        Bitmap bitmapImage = Bitmap.createBitmap(imagePixels, 640, 480, Bitmap.Config.RGB_565);

        Matrix matrix = new Matrix();

        matrix.preScale(-1, 1);

//        matrix.postRotate(90);
//
//        bitmapImage = Bitmap.createBitmap(bitmapImage, 0, 0,
//                640, 480, matrix, false);

        return bitmapImage;
    }

}
