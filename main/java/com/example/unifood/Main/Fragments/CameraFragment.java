package com.example.unifood.Main.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.unifood.Main.Data.Retrieve;
import com.example.unifood.Main.Extension.AlertPermission;
import com.example.unifood.Main.Extension.DateComparison;
import com.example.unifood.Main.Main.MainActivity;
import com.example.unifood.Main.Scanner.CapAct;
import com.example.unifood.Main.Scanner.OpenFoodFactsApiClient;
import com.example.unifood.Main.Scanner.ReadQRCode;
import com.example.unifood.R;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CameraFragment extends Fragment implements SurfaceHolder.Callback, Camera.PictureCallback{

    Button button;
    private static final int CAMERA_REQUEST = 1888;
    String permission;

    private SurfaceHolder surfaceHolder;
    private Camera camera;

    public static final int REQUEST_CODE = 100;

    private SurfaceView surfaceView;
    int cameraID = 1;

    private static final String DATABASE_NAME = "my_database";
    private static final int DATABASE_VERSION = 1;

    // Table name and column names

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_camera_fragment,
                container, false);

        button = view.findViewById(R.id.scan_qr_button);
        surfaceView = view.findViewById(R.id.surfaceArea);

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        cameraID =  Camera.CameraInfo.CAMERA_FACING_BACK;

        requestCameraPermission();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchQRScanner();
            }
        });
        return view;
    }

    private void requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

            // Permission is not granted
            // Check if the user has denied the permission before
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)) {
                AlertPermission.CameraPermission(getActivity(), getContext());
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA},
                        201);
            }
        } else {
            // Permission has already been granted
            // Start the camera preview
            startCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 123: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //If user presses allow
                    startCamera();
                }
                break;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        try {
            startCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startCamera() {
        camera = Camera.open(cameraID);
        camera.setDisplayOrientation(90);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchQRScanner() {
        ScanOptions options = new ScanOptions();
        options.setDesiredBarcodeFormats(ScanOptions.ONE_D_CODE_TYPES);
        options.setPrompt("Scan a barcode");
        options.setCameraId(0);  // Use a specific camera of the device
        options.setBeepEnabled(false);
        options.setOrientationLocked(true);
        options.setBarcodeImageEnabled(true);
        options.setCaptureActivity(CapAct.class);
        barLauncher.launch(options);
    }

    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), scanResult -> {
        if(scanResult.getBarcodeImagePath() != null){
            Log.d("XAS", scanResult.getBarcodeImagePath());

            Result result = ReadQRCode.decodeQR(scanResult.getBarcodeImagePath());
            if(result != null){
                String barcode = result.getText();

                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try  {
                            try {
                                JSONObject productJson = OpenFoodFactsApiClient.getProductInfo(barcode);
                                String productName = productJson.optString("product_name", "Unknown product");
                                String expiryDate = productJson.optString("expiration_date", "Unknown expiry date");
                                String currentDay = DateComparison.currentDay();
                                // Log or display the product and brand names
                                if(productName.equals("Unknown product")) {
                                    Toast.makeText(getActivity(), "failed to get product", Toast.LENGTH_SHORT).show();
                                }else{
                                    if(expiryDate.equals(expiryDate)) expiryDate = DateComparison.defaultExpiry();
                                    Retrieve.insertRow(getActivity(), productName, currentDay, expiryDate);
                                }
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                                Log.d("XAS", e.toString());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();
            }else{
                Log.d("XAS", "null");
            }

        }else{
            Log.d("XAS", "doom");
        }
    });

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        resetCamera();
    }

    public void resetCamera() {
        if (surfaceHolder.getSurface() == null) {
            // Return if preview surface does not exist
            return;
        }

        if (camera != null) {
            // Stop if preview surface is already running.
            camera.stopPreview();
            try {
                // Set preview display
                camera.setPreviewDisplay(surfaceHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Start the camera preview...
            camera.startPreview();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    private void releaseCamera() {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        // Picture had been taken by camera. So, do appropriate action. For example, save it in file.

    }

    @Override
    public void setMenuVisibility(final boolean visible) {
        super.setMenuVisibility(visible);
        if (visible) {

        }
    }
    public static CameraFragment newInstance() {

        CameraFragment f = new CameraFragment();
        Bundle b = new Bundle();

        f.setArguments(b);

        return f;
    }
}
