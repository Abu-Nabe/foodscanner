package com.example.unifood.Main.Scanner;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

public class ReadQRCode {
    public static Result decodeQR(String path) {
        try {
            Bitmap barcodeBitmap = BitmapFactory.decodeFile(path);
            int[] intArray = new int[barcodeBitmap.getWidth() * barcodeBitmap.getHeight()];
            barcodeBitmap.getPixels(intArray, 0, barcodeBitmap.getWidth(), 0, 0, barcodeBitmap.getWidth(), barcodeBitmap.getHeight());
            LuminanceSource source = new RGBLuminanceSource(barcodeBitmap.getWidth(), barcodeBitmap.getHeight(), intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result resultObj = new MultiFormatReader().decode(binaryBitmap);

            return resultObj;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
