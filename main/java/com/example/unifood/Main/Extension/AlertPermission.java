package com.example.unifood.Main.Extension;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;

public class AlertPermission {
    public static void CameraPermission(Activity activity, Context context){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("Allow Camera Access");
        builder1.setMessage("App needs access to your camera. Turn on camera services in your device settings.");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                        intent.setData(uri);
                        context.startActivity(intent);
                    }
                });

        builder1.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
